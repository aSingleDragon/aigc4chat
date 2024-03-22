package pers.hll.aigc4chat.common.protocol.wechat.protocol.response;

import lombok.Builder;
import lombok.Data;


/**
 * 响应消息体
 * window.code=201;window.userAvatar = 'data:img/jpg;base64,/9j/4AAQSkZJRgAB...KKiSRFWD/2Q==';
 *
 * @author hll
 * @since 2023/03/12
 */
@Data
@Builder
public class LoginResp {

    /**
     * window.code=201
     */
    private int code;

    /**
     * window.userAvatar = 'data:img/jpg;base64,/9j/4AAQSkZJRgAB...KKiSRFWD/2Q=='
     */
    private String userAvatar;

    /**
     * 重定向Uri
     */
    private String redirectUri;
}
