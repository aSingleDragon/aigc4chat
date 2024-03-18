package pers.hll.aigc4chat.common.protocol.wechat.protocol.response.webwxinit;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

/**
 *
 * @author hll
 * @since 2024/03/19
 */
@Data
public class SyncKey {

    @SerializedName("Count")
    private int count;

    @SerializedName("List")
    private List<SyncKeyItem> list;

    @Override
    public String toString() {
        StringBuilder sbKey = new StringBuilder();
        for (SyncKeyItem item : list) {
            if (!sbKey.isEmpty()) {
                sbKey.append("|");
            }
            sbKey.append(item.getKey())
                    .append("_")
                    .append(item.getVal());
        }
        return sbKey.toString();
    }
}