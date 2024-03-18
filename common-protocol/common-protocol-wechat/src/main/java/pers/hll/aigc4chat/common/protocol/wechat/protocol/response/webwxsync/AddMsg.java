package pers.hll.aigc4chat.common.protocol.wechat.protocol.response.webwxsync;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class AddMsg {

    @SerializedName("MsgId")
    private long msgId;

    @SerializedName("FromUserName")
    private String fromUserName;

    @SerializedName("ToUserName")
    private String toUserName;

    @SerializedName("MsgType")
    private int msgType;

    @SerializedName("Content")
    private String content;

    @SerializedName("Status")
    private long status;

    @SerializedName("ImgStatus")
    private long imgStatus;

    @SerializedName("CreateTime")
    private long createTime;

    @SerializedName("VoiceLength")
    private long voiceLength;

    @SerializedName("PlayLength")
    private int playLength;

    @SerializedName("FileName")
    private String fileName;

    @SerializedName("FileSize")
    private String fileSize;

    @SerializedName("MediaId")
    private String mediaId;

    @SerializedName("Url")
    private String url;

    @SerializedName("AppMsgType")
    private int appMsgType;

    @SerializedName("StatusNotifyCode")
    private int statusNotifyCode;

    @SerializedName("StatusNotifyUserName")
    private String statusNotifyUserName;

    @SerializedName("RecommendInfo")
    private RecommendInfo recommendInfo;

    @SerializedName("ForwardFlag")
    private int forwardFlag;

    @SerializedName("AppInfo")
    private AppInfo appInfo;

    @SerializedName("HasProductId")
    private int hasProductId;

    @SerializedName("Ticket")
    private String ticket;

    @SerializedName("ImgHeight")
    private int imgHeight;

    @SerializedName("ImgWidth")
    private int imgWidth;

    @SerializedName("SubMsgType")
    private int subMsgType;

    @SerializedName("NewMsgId")
    private long newMsgId;
}