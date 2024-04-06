package pers.hll.aigc4chat.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 微信群成员
 *
 * @author hll
 * @since 2024/04/06
 */
@Data
@TableName("wechat_group_member")
@EqualsAndHashCode(callSuper = true)
public class WeChatGroupMember extends BaseEntity {

    @TableId(type = IdType.ASSIGN_UUID)
    private String uuid;

    /**
     * 群名称
     */
    private String groupUserName;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 名片
     */
    private String displayName;
}