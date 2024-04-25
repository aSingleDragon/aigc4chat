package pers.hll.aigc4chat.protocol.wechat.response;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import pers.hll.aigc4chat.protocol.wechat.response.webwxinit.SyncKey;
import pers.hll.aigc4chat.protocol.wechat.response.webwxinit.User;
import pers.hll.aigc4chat.protocol.wechat.response.webwxinit.MPSubscribeMsg;

import java.util.List;

/**
 * 网页微信初始化请求 响应 body
 *
 * @author hll
 * @author 2024/03/14
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WebWxInitResp extends BaseResponseBaseBody {

    @SerializedName("User")
    private User user;

    @SerializedName("Count")
    private int count;

    @SerializedName("ContactList")
    private List<User> contactList;

    @SerializedName("SyncKey")
    private SyncKey syncKey;

    @SerializedName("ChatSet")
    private String chatSet;

    @SerializedName("SKey")
    private String sKey;

    @SerializedName("ClientVersion")
    private long clientVersion;

    @SerializedName("SystemTime")
    private long systemTime;

    @SerializedName("GrayScale")
    private int grayScale;

    @SerializedName("InviteStartCount")
    private int inviteStartCount;

    @SerializedName("MPSubscribeMsgCount")
    private int mPSubscribeMsgCount;

    @SerializedName("MPSubscribeMsgList")
    private List<MPSubscribeMsg> mPSubscribeMsgList;

    @SerializedName("ClickReportInterval")
    private long clickReportInterval;
}
