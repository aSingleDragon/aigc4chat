package pers.hll.aigc4chat.protocol.wechat.response;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import pers.hll.aigc4chat.protocol.wechat.response.webwxinit.User;

import java.util.List;

/**
 * 更新群聊数据 响应 body
 *
 * @author hll
 * @author 2024/03/10
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class WebWxUpdateChatRoomResp extends BaseResponseBaseBody {

    @SerializedName("MemberCount")
    private int memberCount;

    @SerializedName("MemberList")
    private List<User> memberList;
}
