package pers.hll.aigc4chat.server.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 个人信息
 *
 * @author hll
 * @since 2024/03/19
 */
@Data
@TableName("wechat_user")
@EqualsAndHashCode(callSuper = true)
public class WeChatUser extends BaseEntity {

    private Long uin;

    @TableId
    private String userName;

    private String nickName;

    private String headImgUrl;

    private String remarkName;

    private String pyInitial;

    private String pyQuanPin;

    private String remarkPyInitial;

    private String remarkPyQuanPin;

    private Integer hideInputBarFlag;

    private Integer starFriend;

    private Integer sex;

    private String signature;

    private Integer appAccountFlag;

    private Integer verifyFlag;

    private Integer contactFlag;

    private Integer webWxPluginSwitch;

    private Integer headImgFlag;

    private Integer snsFlag;

    private Long ownerUin;

    private Integer memberCount;

    private Integer statues;

    private Long attrStatus;

    private Integer memberStatus;

    private String province;

    private String city;

    private String alias;

    private Long uniFriend;

    private String displayName;

    private Long chatRoomId;

    private String keyWord;

    private Integer isOwner;

    private String encryptChatRoomId;
}