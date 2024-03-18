package pers.hll.aigc4chat.common.protocol.wechat.protocol.response.webwxsync;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class ProfileItem {

    @SerializedName("Buff")
    private String buff;
}