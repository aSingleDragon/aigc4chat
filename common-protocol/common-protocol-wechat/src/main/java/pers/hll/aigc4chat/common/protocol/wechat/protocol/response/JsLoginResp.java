package pers.hll.aigc4chat.common.protocol.wechat.protocol.response;

import lombok.Builder;
import lombok.Data;


/**
 * jsLogin请求响应 body
 * window.QRLogin.code = 200; window.QRLogin.uuid = "YdhFiz5kzw==";
 *
 * @author hll
 * @since 2024/03/12
 */
@Data
@Builder
public class JsLoginResp {

    /**
     * window.QRLogin.code = 200
     */
    private int code;

    /**
     * window.QRLogin.uuid = "YdhFiz5kzw=="
     */
    private String uuid;
}
