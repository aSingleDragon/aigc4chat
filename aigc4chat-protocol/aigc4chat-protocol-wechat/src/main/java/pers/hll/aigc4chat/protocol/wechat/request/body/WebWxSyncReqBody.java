package pers.hll.aigc4chat.protocol.wechat.request.body;


import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import pers.hll.aigc4chat.protocol.wechat.response.webwxinit.SyncKey;

/**
 * 同步数据请求 body
 *
 * @author hll
 * @since 2024/03/19
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class WebWxSyncReqBody extends BasePostRequestBaseBody {

    @SerializedName("SyncKey")
    private SyncKey syncKey;

    @SerializedName("rr")
    private long rr;
}
