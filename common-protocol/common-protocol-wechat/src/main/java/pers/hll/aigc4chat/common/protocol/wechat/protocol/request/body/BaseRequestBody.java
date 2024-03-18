package pers.hll.aigc4chat.common.protocol.wechat.protocol.request.body;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.RequiredArgsConstructor;

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
