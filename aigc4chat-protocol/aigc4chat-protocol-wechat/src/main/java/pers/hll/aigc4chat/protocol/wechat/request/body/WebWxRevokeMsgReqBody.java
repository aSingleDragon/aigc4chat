package pers.hll.aigc4chat.protocol.wechat.request.body;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 撤回消息请求 body
 *
 * @author hll
 * @since 2024/03/19
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class WebWxRevokeMsgReqBody extends BasePostRequestBaseBody {

    @SerializedName("ClientMsgId")
    public String clientMsgId;

    @SerializedName("SvrMsgId")
    public String svrMsgId;

    @SerializedName("ToUserName")
    public String toUserName;
}