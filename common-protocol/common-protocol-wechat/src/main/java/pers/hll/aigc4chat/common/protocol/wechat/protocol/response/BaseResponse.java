package pers.hll.aigc4chat.common.protocol.wechat.protocol.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.RequiredArgsConstructor;

/**
 *
 * @author hll
 * @author 2024/03/10
 */
@RequiredArgsConstructor
public class BaseResponse {

    @JsonProperty("Ret")
    public final int ret;

    @JsonProperty("ErrMsg")
    public final String errMsg;
}