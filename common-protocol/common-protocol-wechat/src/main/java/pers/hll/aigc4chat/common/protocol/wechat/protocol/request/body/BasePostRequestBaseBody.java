package pers.hll.aigc4chat.common.protocol.wechat.protocol.request.body;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

/**
 *
 * @author hll
 * @since 2024/03/19
 */
@Data
@Accessors(chain = true)
public class BasePostRequestBaseBody {

    @SerializedName("BaseRequest")
    private BaseRequestBody baseRequestBody;
}
