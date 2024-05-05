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
}
