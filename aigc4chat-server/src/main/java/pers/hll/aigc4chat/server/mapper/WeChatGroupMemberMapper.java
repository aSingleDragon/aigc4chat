package pers.hll.aigc4chat.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import pers.hll.aigc4chat.server.bean.WeChatUserPageQuery;
import pers.hll.aigc4chat.server.entity.WeChatGroupMember;
import pers.hll.aigc4chat.server.entity.WeChatUser;

import java.util.List;

/**
 * <p>
 * 群成员 Mapper 接口
 * </p>
 *
 * @author hll
 * @since 2024/03/31
 */
public interface WeChatGroupMemberMapper extends BaseMapper<WeChatGroupMember> {

    IPage<WeChatUser> pageGroupMember(@Param("query") WeChatUserPageQuery query, IPage<WeChatUser> page);

    List<WeChatUser> listGroupMember(String groupUserName);
}
