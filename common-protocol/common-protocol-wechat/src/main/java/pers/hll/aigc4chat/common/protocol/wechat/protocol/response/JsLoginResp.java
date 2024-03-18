package pers.hll.aigc4chat.common.protocol.wechat.protocol.response;

import lombok.Builder;
import lombok.Data;


/**
 * 响应消息体
 * window.QRLogin.code = 200; window.QRLogin.uuid = "YdhFiz5kzw==";
 *
 * @author hll
 * @since 2023/03/12
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
