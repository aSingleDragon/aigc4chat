package pers.hll.aigc4chat.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import pers.hll.aigc4chat.server.entity.WeChatGroupMember;

import java.util.List;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author hll
 * @since 2024/03/31
 */
public interface IWeChatGroupMemberService extends IService<WeChatGroupMember> {


    List<WeChatGroupMember> listByGroupUserName(String groupUserName);
}
