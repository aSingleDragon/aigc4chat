package pers.hll.aigc4chat.server.service.impl;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pers.hll.aigc4chat.server.service.*;

/**
 * 登录服务实现
 *
 * @author hll
 * @since 2024/03/31
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WeChatLoginServiceImpl implements IWeChatLoginService {

    private final IWeChatApiService weChatApiService;

    private final AsyncLoginService asyncLoginService;
    @Override
    public void login(HttpServletResponse response) {
        weChatApiService.jsLogin(response);
        asyncLoginService.login();
    }

    @Override
    public void logout() {
        weChatApiService.logout();
    }

}
