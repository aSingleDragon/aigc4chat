package pers.hll.aigc4chat.common.protocol.wechat.protocol.response;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * 同步检查响应
 *
 * @author hll
 * @author 2024/03/14
 */
@Data
@RequiredArgsConstructor
public class SyncCheckResp {

    /**
     * 响应码
     * <ul>
     *     <li>0 : 成功</li>
     *     <li>其他 : 失败</li>
     * </ul>
     */
    @SerializedName("retcode")
    private final Integer retCode;

    private final Integer selector;
}
