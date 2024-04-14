package pers.hll.aigc4chat.common.protocol.wechat.protocol.request.body;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * 微信 Post 类型的接口 基本都要有这部分内容用来鉴权
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
}
