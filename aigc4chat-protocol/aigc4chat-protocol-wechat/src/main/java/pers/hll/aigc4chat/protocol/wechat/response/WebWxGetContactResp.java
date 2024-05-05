package pers.hll.aigc4chat.protocol.wechat.response;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import pers.hll.aigc4chat.protocol.wechat.response.webwxinit.User;

import java.util.List;

/**
 * 获取联系人列表 响应 body
 *
 * @author hll
 * @author 2024/03/14
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WebWxGetContactResp extends BaseResponseBaseBody {

    @SerializedName("MemberCount")
    private int memberCount;

    @SerializedName("MemberList")
    private List<User> memberList;

    @SerializedName("Seq")
    private int seq;
}
