package pers.hll.aigc4chat.common.protocol.wechat.protocol.request.body;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 *
 * @author hll
 * @since 2024/03/19
 */
@Data
@RequiredArgsConstructor
public class BaseRequestBody {

    @SerializedName("Uin")
    public final String uin;

    @SerializedName("Sid")
    public final String sId;

    @SerializedName("Skey")
    public final String sKey;

    //@JsonProperty("DeviceID")
    //public final String deviceID;
}
