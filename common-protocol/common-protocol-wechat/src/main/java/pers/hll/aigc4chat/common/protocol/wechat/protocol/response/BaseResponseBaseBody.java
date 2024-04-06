package pers.hll.aigc4chat.common.protocol.wechat.protocol.response;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * 响应 body 的公共部分
 *
 * @author hll
 * @author 2024/03/10
 */
@Data
public class BaseResponseBaseBody {

    @SerializedName("BaseResponse")
    private BaseResponseBody baseResponseBody;

    public boolean isSuccess() {
        return baseResponseBody != null && baseResponseBody.getRet() == 0;
    }
}