package pers.hll.aigc4chat.common.protocol.wechat.protocol.response.webwxsync;

import com.google.gson.annotations.SerializedName;

public class Profile {

    @SerializedName("BitFlag")
    private int bitFlag;

    @SerializedName("UserName")
    private ProfileItem userName;

    @SerializedName("NickName")
    private ProfileItem nickName;

    @SerializedName("BindUin")
    private long bindUin;

    @SerializedName("BindEmail")
    private ProfileItem bindEmail;

    @SerializedName("BindMobile")
    private ProfileItem bindMobile;

    @SerializedName("Status")
    private long status;

    @SerializedName("Sex")
    private int sex;

    @SerializedName("PersonalCard")
    private int personalCard;

    @SerializedName("Alias")
    private String alias;

    @SerializedName("HeadImgUpdateFlag")
    private int headImgUpdateFlag;

    @SerializedName("HeadImgUrl")
    private String headImgUrl;

    @SerializedName("Signature")
    private String signature;
}