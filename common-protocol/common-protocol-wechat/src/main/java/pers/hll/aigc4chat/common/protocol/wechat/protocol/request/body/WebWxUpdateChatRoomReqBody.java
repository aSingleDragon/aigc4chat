package pers.hll.aigc4chat.common.protocol.wechat.protocol.request.body;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 更新群聊数据请求 body
 *
 * @author hll
 * @author 2024/03/10
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class WebWxUpdateChatRoomReqBody extends BasePostRequestBaseBody {

    @SerializedName("ChatRoomName")
    private String chatRoomName;

    @SerializedName("NewTopic")
    private String newTopic;

    @SerializedName("AddMemberList")
    private String addMemberList;

    @SerializedName("DelMemberList")
    private String delMemberList;
}
