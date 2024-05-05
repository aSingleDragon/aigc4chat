package pers.hll.aigc4chat.server.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import pers.hll.aigc4chat.server.bean.WeChatUserPageQuery;
import pers.hll.aigc4chat.server.entity.WeChatGroupMember;
import pers.hll.aigc4chat.server.entity.WeChatUser;

import java.util.List;

/**
 * <p>
 * 群成员 接口
 * </p>
 *
 * @author hll
 * @since 2024/03/31
 */
public interface IWeChatGroupMemberService extends IService<WeChatGroupMember> {


    List<WeChatGroupMember> listByGroupUserName(String groupUserName);

    IPage<WeChatUser> pageGroupMember(WeChatUserPageQuery query);

    List<WeChatUser> listGroupMember(String groupUserName);
}
