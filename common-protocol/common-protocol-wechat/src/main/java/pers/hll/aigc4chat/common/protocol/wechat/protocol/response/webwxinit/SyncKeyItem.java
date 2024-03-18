package pers.hll.aigc4chat.common.protocol.wechat.protocol.response.webwxinit;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class SyncKeyItem {

    @SerializedName("Key")
    private int key;

    @SerializedName("Val")
    private int val;
}