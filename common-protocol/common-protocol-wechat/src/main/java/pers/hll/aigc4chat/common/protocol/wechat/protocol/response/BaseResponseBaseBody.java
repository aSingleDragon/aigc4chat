package pers.hll.aigc4chat.common.protocol.wechat.protocol.response;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hll
 * @author 2024/03/10
 */
@Data
public class BaseResponseBaseBody {

    @SerializedName("BaseResponse")
    private BaseResponseBody baseResponseBody;
}