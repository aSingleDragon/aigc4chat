package pers.hll.aigc4chat.common.protocol.wechat.protocol.request.body;


import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
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
