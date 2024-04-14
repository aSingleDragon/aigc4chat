package pers.hll.aigc4chat.common.protocol.wechat.protocol.response.webwxsync;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * 简介项
 *
 * @author hll
 * @since 2024/03/19
 */
@Data
public class ProfileItem {

    @SerializedName("Buff")
    private String buff;
}