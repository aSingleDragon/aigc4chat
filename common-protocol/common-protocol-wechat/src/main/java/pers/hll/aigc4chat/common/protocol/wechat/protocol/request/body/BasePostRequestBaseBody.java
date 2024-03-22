package pers.hll.aigc4chat.common.protocol.wechat.protocol.request.body;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 微信 Post 类型的接口 基本都要有这部分内容用来鉴权
 * <blockquote><pre>
 * {
 *     "BaseRequest": {
 *         "Uin": "123456789",
 *         "Sid": "123456789",
 *         "Skey": "123456789"
 *     }
 *     ...
 * }
 * </pre></blockquote>
 *
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
