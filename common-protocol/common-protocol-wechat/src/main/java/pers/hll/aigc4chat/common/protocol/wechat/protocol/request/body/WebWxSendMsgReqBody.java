package pers.hll.aigc4chat.common.protocol.wechat.protocol.request.body;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 *
 * @author hll
 * @author 2024/03/10
 */
@Data
@Accessors(chain = true)
public class WebWxSendMsgReqBody extends BasePostRequestBaseBody {

    @SerializedName("Msg")
    private Msg msg;

    @SerializedName("Scene")
    private int scene = 0;
}
