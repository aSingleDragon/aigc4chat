package pers.hll.aigc4chat.server.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import pers.hll.aigc4chat.protocol.wechat.request.body.Contact;
import pers.hll.aigc4chat.server.bean.WeChatUserPageQuery;
import pers.hll.aigc4chat.server.entity.WeChatUser;

import java.util.List;

/**
 * <p>
 * 用户 接口
 * </p>
 *
 * @author hll
 * @since 2024/03/31
 */
public interface IWeChatUserService extends IService<WeChatUser> {

    void saveOrUpdateMe(WeChatUser me);

    WeChatUser selectMe();

    IPage<WeChatUser> pageGroup(WeChatUserPageQuery query);

    IPage<WeChatUser> pageFriend(WeChatUserPageQuery query);

    List<WeChatUser> listByName(String name);

    String getOneByName(String name);

    /**
     * 获取并保存不限数量和类型的联系人信息
     *
     * @param userNames 逗号分隔的联系人userName
     */
    void loadContacts(String userNames, boolean useCache);

    /**
     * 获取并保存不限数量和类型的联系人信息
     *
     * @param contacts 要获取的联系人的列表，数量和类型不限
     */
    void loadContacts(List<Contact> contacts, boolean useCache);
}
