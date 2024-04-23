package pers.hll.aigc4chat.server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pers.hll.aigc4chat.server.base.R;
import pers.hll.aigc4chat.server.entity.WechatMessageHandlerConfig;
import pers.hll.aigc4chat.server.service.IWeChatUserService;
import pers.hll.aigc4chat.server.service.IWechatMessageHandlerConfigService;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author hll
 * @since 2024-04-22
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/message-handler")
public class WechatMessageHandlerConfigController {

    private final IWechatMessageHandlerConfigService weChatMessageHandlerConfigService;

    private final IWeChatUserService weChatUserService;

    @GetMapping("/list/handler-name")
    public R<List<String>> listHandlerName() {
        return R.data(weChatMessageHandlerConfigService.listHandlerName());
    }

    @PostMapping("/save-or-update")
    public R<String> saveOrUpdate(@RequestParam String userName, @RequestParam String handlerName) {
        if (!weChatMessageHandlerConfigService.listHandlerName().contains(handlerName)) {
            throw new IllegalArgumentException("未找到[" + handlerName + "]对应的消息处理器!");
        }
        weChatMessageHandlerConfigService.saveOrUpdate(new WechatMessageHandlerConfig(
                weChatUserService.getOneByName(userName),
                handlerName));
        return R.success();
    }

    @GetMapping("/get-handler-name")
    public R<?> getHandlerName(@RequestParam String name) {
        return R.data(weChatMessageHandlerConfigService.getHandlerName(weChatUserService.getOneByName(name)));
    }
}
