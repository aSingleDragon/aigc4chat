package pers.hll.aigc4chat.common.protocol.wechat.protocol.request.body;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class WebWxRevokeMsgReqBody extends BasePostRequestBaseBody {

    @SerializedName("ClientMsgId")
    public String clientMsgId;

    @SerializedName("SvrMsgId")
    public String svrMsgId;

    @SerializedName("ToUserName")
    public String toUserName;
}