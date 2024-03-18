package pers.hll.aigc4chat.common.protocol.wechat.protocol.request.body;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author hll
 * @since 2024/03/19
 */
@Data
@AllArgsConstructor
public class VerifyUser {

    @SerializedName("Value")
    private String value;

    @SerializedName("VerifyUserTicket")
    private String verifyUserTicket;
}