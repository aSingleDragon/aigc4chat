package pers.hll.aigc4chat.common.protocol.wechat.protocol.response.webwxsync;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class RecommendInfo {

    @SerializedName("UserName")
    private String userName;

    @SerializedName("NickName")
    private String nickName;

    @SerializedName("QQNum")
    private long qqNum;

    @SerializedName("Province")
    private String province;

    @SerializedName("City")
    private String city;

    @SerializedName("Content")
    private String content;

    @SerializedName("Signature")
    private String signature;

    @SerializedName("Alias")
    private String alias;

    @SerializedName("Scene")
    private int scene;

    @SerializedName("VerifyFlag")
    private int verifyFlag;

    @SerializedName("AttrStatus")
    private long attrStatus;

    @SerializedName("Sex")
    private int sex;

    @SerializedName("Ticket")
    private String ticket;

    @SerializedName("OpCode")
    private int opCode;
}