package pers.hll.aigc4chat.common.protocol.wechat.protocol.response;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.response.webwxinit.User;

import java.util.ArrayList;
import java.util.List;

/**
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
