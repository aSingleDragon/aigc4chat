package pers.hll.aigc4chat.protocol.wechat.request.body;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * 微信 Post 类型的接口 基本都要有这部分内容用来鉴权
 * <p>IDEA 总是提示将这个类转换成 {@code record} 但是因为 {@link SerializedName} 要序列化 不能转成 {@code record}
 * 所以增加了 {@link SuppressWarnings} 消除这个提示
 *
 * @author hll
 * @since 2024/03/19
 */
@Data
@RequiredArgsConstructor
@SuppressWarnings({"all"})
public class BaseRequestBody {

    @SerializedName("Uin")
    public final String uin;

    @SerializedName("Sid")
    public final String sId;

    @SerializedName("Skey")
    public final String sKey;
}
