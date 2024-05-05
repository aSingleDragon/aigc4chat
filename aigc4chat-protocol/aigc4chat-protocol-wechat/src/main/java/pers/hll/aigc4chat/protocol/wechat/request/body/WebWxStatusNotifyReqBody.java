package pers.hll.aigc4chat.protocol.wechat.request.body;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 状态同步请求 body
 *
 * @author hll
 * @since 2024/03/19
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class WebWxStatusNotifyReqBody extends BasePostRequestBaseBody {

    @SerializedName("Code")
    private int code;

    @SerializedName("FromUserName")
    private String fromUserName;

    @SerializedName("ToUserName")
    private String toUserName;

    @SerializedName("ClientMsgId")
    private long clientMsgId;
}
