package pers.hll.aigc4chat.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import pers.hll.aigc4chat.server.bean.WeChatUserPageQuery;
import pers.hll.aigc4chat.server.entity.BaseEntity;
import pers.hll.aigc4chat.server.entity.WeChatUser;
import pers.hll.aigc4chat.server.mapper.WeChatUserMapper;
import pers.hll.aigc4chat.server.service.IWeChatUserService;
import pers.hll.aigc4chat.server.util.PageUtil;

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
}
