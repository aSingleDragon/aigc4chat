package pers.hll.aigc4chat.common.protocol.wechat.protocol.response.webwxsync;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 *
 * @author hll
 * @since 2024/03/19
 */
@Data
public class AppInfo {

    @SerializedName("AppID")
    private String appId;

    @SerializedName("Type")
    private int type;
}