package pers.hll.aigc4chat.common.protocol.wechat.protocol.response.webwxsync;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * 小程序信息
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