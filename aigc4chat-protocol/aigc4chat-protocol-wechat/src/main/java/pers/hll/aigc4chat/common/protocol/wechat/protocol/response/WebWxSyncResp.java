package pers.hll.aigc4chat.common.protocol.wechat.protocol.response;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.response.webwxinit.SyncKey;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.response.webwxinit.User;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.response.webwxsync.AddMsg;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.response.webwxsync.Profile;

import java.util.List;

/**
 * 同步数据 响应 body
 *
 * @author hll
 * @author 2024/03/14
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WebWxSyncResp extends BaseResponseBaseBody {

    @SerializedName("AddMsgCount")
    private int addMsgCount;

    @SerializedName("AddMsgList")
    private List<AddMsg> addMsgList;

    @SerializedName("ModContactCount")
    private int modContactCount;

    @SerializedName("ModContactList")
    private List<User> modContactList;

    @SerializedName("DelContactCount")
    private int delContactCount;

    @SerializedName("DelContactList")
    private List<User> delContactList;

    @SerializedName("ModChatRoomMemberCount")
    private int modChatRoomMemberCount;

    @SerializedName("ModChatRoomMemberList")
    private List<User> modChatRoomMemberList;

    @SerializedName("Profile")
    private Profile profile;

    @SerializedName("ContinueFlag")
    private int continueFlag;

    @SerializedName("SyncKey")
    private SyncKey syncKey;

    @SerializedName("Skey")
    private String sKey;

    @SerializedName("SyncCheckKey")
    private SyncKey syncCheckKey;
}
