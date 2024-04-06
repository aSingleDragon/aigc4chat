package pers.hll.aigc4chat.common.protocol.wechat.protocol.response.webwxinit;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

/**
 * 个人信息
 *
 * @author hll
 * @since 2024/03/19
 */
@Data
public class User {

    @SerializedName("Uin")
    private Long uin;

    @SerializedName("UserName")
    private String userName;

    @SerializedName("NickName")
    private String nickName;

    @SerializedName("HeadImgUrl")
    private String headImgUrl;

    @SerializedName("RemarkName")
    private String remarkName;

    @SerializedName("PYInitial")
    private String pyInitial;

    @SerializedName("PYQuanPin")
    private String pyQuanPin;

    @SerializedName("RemarkPYInitial")
    private String remarkPyInitial;

    @SerializedName("RemarkPYQuanPin")
    private String remarkPyQuanPin;

    @SerializedName("HideInputBarFlag")
    private Integer hideInputBarFlag;

    @SerializedName("StarFriend")
    private Integer starFriend;

    @SerializedName("Sex")
    private Integer sex;

    @SerializedName("Signature")
    private String signature;

    @SerializedName("AppAccountFlag")
    private Integer appAccountFlag;

    @SerializedName("VerifyFlag")
    private Integer verifyFlag;

    @SerializedName("ContactFlag")
    private Integer contactFlag;

    @SerializedName("WebWxPluginSwitch")
    private Integer webWxPluginSwitch;

    @SerializedName("HeadImgFlag")
    private Integer headImgFlag;

    @SerializedName("SnsFlag")
    private Integer snsFlag;

    @SerializedName("OwnerUin")
    private Long ownerUin;

    @SerializedName("MemberCount")
    private Integer memberCount;

    @SerializedName("MemberList")
    private List<User> memberList;

    @SerializedName("Statues")
    private Integer statues;

    @SerializedName("AttrStatus")
    private Long attrStatus;

    @SerializedName("MemberStatus")
    private Integer memberStatus;

    @SerializedName("Province")
    private String province;

    @SerializedName("City")
    private String city;

    @SerializedName("Alias")
    private String alias;

    @SerializedName("UniFriend")
    private Long uniFriend;

    @SerializedName("DisplayName")
    private String displayName;

    @SerializedName("ChatRoomId")
    private Long chatRoomId;

    @SerializedName("KeyWord")
    private String keyWord;

    @SerializedName("IsOwner")
    private Integer isOwner;

    @SerializedName("EncryChatRoomId")
    private String encryptChatRoomId;
}