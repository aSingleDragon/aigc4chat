package pers.hll.aigc4chat.server.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.response.LoginResp;
import pers.hll.aigc4chat.server.base.R;
import pers.hll.aigc4chat.server.service.IWeChatLoginService;

/**
 * 登录控制器
 *
 * @author hll
 * @since 2024/03/21
 */
@RestController
@RequiredArgsConstructor
public class LoginController {

    private final IWeChatLoginService wechatLoginService;

    @GetMapping("/login")
    public R<LoginResp> login() {
        wechatLoginService.login();
        return R.success();
    }

    @GetMapping("/logout")
    public R<LoginResp> logout() {
        wechatLoginService.login();
        return R.success();
    }
}
