package pers.hll.aigc4chat.protocol.wechat.request.body;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 微信登出请求 body
 *
 * @author hll
 * @since 2024/03/19
 */
@Data
@AllArgsConstructor
public class WebWxLogoutReqBody {

    private String sid;

    private String uin;
}
