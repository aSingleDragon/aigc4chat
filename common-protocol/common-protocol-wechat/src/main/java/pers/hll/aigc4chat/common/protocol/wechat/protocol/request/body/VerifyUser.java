package pers.hll.aigc4chat.common.protocol.wechat.protocol.request.body;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VerifyUser {

    @SerializedName("Value")
    private String value;

    @SerializedName("VerifyUserTicket")
    private String verifyUserTicket;
}