package pers.hll.aigc4chat.server.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pers.hll.aigc4chat.protocol.wechat.response.LoginResp;
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
@RequestMapping("/wechat")
@Tag(name = "LoginController", description = "登录控制器")
public class LoginController {

    private final IWeChatLoginService wechatLoginService;

    @GetMapping("/login")
    @Operation(summary = "登录", description = "生成二维码，使用微信扫码登录。")
    public void login(HttpServletResponse response) {
        wechatLoginService.login(response);
    }

    @GetMapping("/logout")
    @Operation(summary = "登出", description = "")
    public R<LoginResp> logout() {
        wechatLoginService.logout();
        return R.success();
    }
}
