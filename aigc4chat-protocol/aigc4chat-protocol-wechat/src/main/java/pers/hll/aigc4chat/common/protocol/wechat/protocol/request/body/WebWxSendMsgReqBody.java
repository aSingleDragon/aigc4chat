package pers.hll.aigc4chat.common.protocol.wechat.protocol.request.body;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 发送消息请求 body
 *
 * @author hll
 * @author 2024/03/10
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class WebWxSendMsgReqBody extends BasePostRequestBaseBody {

    @SerializedName("Msg")
    private Msg msg;

    @SerializedName("Scene")
    private int scene = 0;
}
