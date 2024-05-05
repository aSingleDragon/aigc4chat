package pers.hll.aigc4chat.protocol.wechat.response;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 状态通知响应 body
 *
 * @author hll
 * @author 2024/03/14
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WebWxStatusNotifyResp extends BaseResponseBaseBody {

    @SerializedName("MsgID")
    public String msgId;
}
