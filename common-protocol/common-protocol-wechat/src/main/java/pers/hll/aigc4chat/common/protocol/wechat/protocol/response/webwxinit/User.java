package pers.hll.aigc4chat.common.protocol.wechat.protocol.response.webwxinit;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

/**
 *
 * @author hll
 * @since 2024/03/19
 */
@Data
public class User {

    @SerializedName("Uin")
    private long uin;

    @SerializedName("UserName")
    private String userName;

    @SerializedName("NickName")
    private String nickName;

    @SerializedName("HeadImgUrl")
    private String headImgUrl;

    @SerializedName("RemarkName")
    private String remarkName;

    @SerializedName("PYInitial")
    private String pYInitial;

    @SerializedName("PYQuanPin")
    private String pYQuanPin;

    @SerializedName("RemarkPYInitial")
    private String remarkPYInitial;

    @SerializedName("RemarkPYQuanPin")
    private String remarkPYQuanPin;

    @SerializedName("HideInputBarFlag")
    private int hideInputBarFlag;

    @SerializedName("StarFriend")
    private int starFriend;

    @SerializedName("Sex")
    private int sex;

    @SerializedName("Signature")
    private String signature;

    @SerializedName("AppAccountFlag")
    private int appAccountFlag;

    @SerializedName("VerifyFlag")
    private int verifyFlag;

    @SerializedName("ContactFlag")
    private int contactFlag;

    @SerializedName("WebWxPluginSwitch")
    private int webWxPluginSwitch;

    @SerializedName("HeadImgFlag")
    private int headImgFlag;

    @SerializedName("SnsFlag")
    private int snsFlag;

    @SerializedName("OwnerUin")
    private long ownerUin;

    @SerializedName("MemberCount")
    private int memberCount;

    @SerializedName("MemberList")
    private List<User> memberList;

    @SerializedName("Statues")
    private long statues;

    @SerializedName("AttrStatus")
    private long attrStatus;

    @SerializedName("MemberStatus")
    private long memberStatus;

    @SerializedName("Province")
    private String province;

    @SerializedName("City")
    private String city;

    @SerializedName("Alias")
    private String alias;

    @SerializedName("UniFriend")
    private long uniFriend;

    @SerializedName("DisplayName")
    private String displayName;

    @SerializedName("ChatRoomId")
    private long chatRoomId;

    @SerializedName("KeyWord")
    private String keyWord;

    @SerializedName("IsOwner")
    private int isOwner;

    @SerializedName("EncryChatRoomId")
    private String encryChatRoomId;
}