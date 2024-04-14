package pers.hll.aigc4chat.common.protocol.wechat.protocol.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 微信登录状态
 *
 * @author hll
 * @since 2024/03/31
 */
@Getter
@RequiredArgsConstructor
public enum WeChatLoginStatus {

    AUTHORIZED_LOGIN(200, "已授权登录!"),

    QR_CODE_SCANNED(201, "已扫描二维码!"),

    WAITING_FOR_AUTHORIZATION(408, "等待授权登录..."),

    LOGIN_TIMEOUT(500, "登录超时!");

    private final int code;

    private final String msg;

    public static WeChatLoginStatus ofCode(int code) {
        for (WeChatLoginStatus status : WeChatLoginStatus.values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        return LOGIN_TIMEOUT;
    }
}
