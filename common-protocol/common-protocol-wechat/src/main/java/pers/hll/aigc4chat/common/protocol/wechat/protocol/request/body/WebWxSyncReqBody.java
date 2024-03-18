package pers.hll.aigc4chat.common.protocol.wechat.protocol.request.body;


import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.experimental.Accessors;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.response.webwxinit.SyncKey;

@Data
@Accessors(chain = true)
public class WebWxSyncReqBody extends BasePostRequestBaseBody {

    @SerializedName("SyncKey")
    private SyncKey syncKey;

    @SerializedName("rr")
    private long rr;
}
