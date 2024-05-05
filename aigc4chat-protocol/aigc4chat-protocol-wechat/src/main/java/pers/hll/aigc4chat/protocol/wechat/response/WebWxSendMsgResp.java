package pers.hll.aigc4chat.protocol.wechat.response;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 发送消息 响应 body
 *
 * @author hll
 * @author 2024/03/14
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WebWxSendMsgResp extends BaseResponseBaseBody {

    @SerializedName("MsgID")
    private String msgId;

    @SerializedName("LocalID")
    private String localId;
}
