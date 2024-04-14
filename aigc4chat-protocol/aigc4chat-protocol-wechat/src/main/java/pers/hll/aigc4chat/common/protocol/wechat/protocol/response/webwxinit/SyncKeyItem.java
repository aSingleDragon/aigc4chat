package pers.hll.aigc4chat.common.protocol.wechat.protocol.response.webwxinit;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * 同步的 key 项
 *
 * @author hll
 * @since 2024/03/19
 */
@Data
public class SyncKeyItem {

    @SerializedName("Key")
    private int key;

    @SerializedName("Val")
    private int val;
}