package pers.hll.aigc4chat.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import pers.hll.aigc4chat.base.exception.BizException;
import pers.hll.aigc4chat.base.util.StringUtil;
import pers.hll.aigc4chat.protocol.wechat.request.body.Contact;
import pers.hll.aigc4chat.server.bean.WeChatUserPageQuery;
import pers.hll.aigc4chat.server.entity.BaseEntity;
import pers.hll.aigc4chat.server.entity.WeChatUser;
import pers.hll.aigc4chat.server.mapper.WeChatUserMapper;
import pers.hll.aigc4chat.server.service.IWeChatApiService;
import pers.hll.aigc4chat.server.service.IWeChatUserService;
import pers.hll.aigc4chat.server.util.PageUtil;
import pers.hll.aigc4chat.server.wechat.WeChatTool;

import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author hll
 * @since 2024-04-14
 */
@Service
@RequiredArgsConstructor
public class WeChatUserServiceImpl extends ServiceImpl<WeChatUserMapper, WeChatUser> implements IWeChatUserService {

    private final WeChatUserMapper weChatUserMapper;

    private final IWeChatApiService weChatApiService;

    @Override
    public void saveOrUpdateMe(WeChatUser me) {
        // 每次获取的用户的userName都会变 登录的用户的uin不会变 删除旧的用户
        list(new LambdaQueryWrapper<WeChatUser>().eq(WeChatUser::getUin, me.getUin())).forEach(this::removeById);
        save(me);
    }

    @Override
    public WeChatUser selectMe() {
        return getOne(new LambdaQueryWrapper<WeChatUser>().gt(WeChatUser::getUin, 0));
    }

    @Override
    public IPage<WeChatUser> pageGroup(WeChatUserPageQuery query) {
        IPage<WeChatUser> page = PageUtil.createPage(query);
        return weChatUserMapper.selectPage(page, new LambdaQueryWrapper<WeChatUser>()
                .like(WeChatUser::getUserName, "@@%")
                .like(StringUtils.isNotEmpty(query.getUserName()), WeChatUser::getNickName, query.getUserName())
                .orderByAsc(BaseEntity::getUpdatedTime));
    }

    @Override
    public IPage<WeChatUser> pageFriend(WeChatUserPageQuery query) {
        IPage<WeChatUser> page = PageUtil.createPage(query);
        return weChatUserMapper.selectPage(page, new LambdaQueryWrapper<WeChatUser>()
                .like(WeChatUser::getUserName, "@%")
                .eq(WeChatUser::getVerifyFlag, 0)
                .notLike(WeChatUser::getUserName, "@@%")
                .like(StringUtils.isNotEmpty(query.getUserName()), WeChatUser::getNickName, query.getUserName())
                .orderByAsc(BaseEntity::getUpdatedTime));
    }

    @Override
    public List<WeChatUser> listByName(String name) {
        return list(new LambdaQueryWrapper<WeChatUser>()
                .like(WeChatUser::getUserName, name)
                .or()
                .like(WeChatUser::getNickName, name)
                .or()
                .like(WeChatUser::getRemarkName, name));
    }

    @Override
    public String getOneByName(String name) {
        List<WeChatUser> weChatUserList = listByName(name);
        if (weChatUserList.isEmpty()) {
            throw BizException.of("未找到[{}]对应的微信用户!", name);
        }
        if (weChatUserList.size() > 1) {
            throw BizException.of("找到多个[{}]对应的微信用户!", name);
        }
        return weChatUserList.get(0).getUserName();
    }

    /**
     * 获取并保存不限数量和类型的联系人信息
     *
     * @param userNames 逗号分隔的联系人userName
     */
    @Override
    public void loadContacts(String userNames, boolean useCache) {
        if (StringUtils.isNotEmpty(userNames)) {
            List<Contact> contactList = StringUtil.splitToList(userNames).stream().map(Contact::new).toList();
            loadContacts(new LinkedList<>(contactList), useCache);
        }
    }

    /**
     * 获取并保存不限数量和类型的联系人信息
     *
     * @param contacts 要获取的联系人的列表，数量和类型不限
     */
    @Override
    public void loadContacts(List<Contact> contacts, boolean useCache) {
        if (useCache) {
            // 不是群聊，并且已经获取过，就不再次获取
            contacts.removeIf(contact -> WeChatTool.isNotGroup(contact.getUserName())
                    && getById(contact.getUserName()) != null);
        }
        // 拆分成每次50个联系人分批获取
        if (contacts.size() > 50) {
            LinkedList<Contact> temp = new LinkedList<>();
            for (Contact contact : contacts) {
                temp.add(contact);
                if (temp.size() >= 50) {
                    weChatApiService.webWxBatchGetContact(contacts);
                    temp.clear();
                }
            }
            contacts = temp;
        }
        if (!contacts.isEmpty()) {
            weChatApiService.webWxBatchGetContact(contacts);
        }
    }
}
