package pers.hll.aigc4chat.server.service;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 登录接口
 *
 * @author hll
 * @since 2024/03/31
 */
public interface IWeChatLoginService {

    void login(HttpServletResponse response);

    void logout();
}
