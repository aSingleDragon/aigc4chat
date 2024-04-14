package pers.hll.aigc4chat.server.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.response.LoginResp;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.response.SyncCheckResp;
import pers.hll.aigc4chat.server.base.R;
import pers.hll.aigc4chat.server.bean.WeChatMessagePageQuery;
import pers.hll.aigc4chat.server.entity.WeChatMessage;
import pers.hll.aigc4chat.server.service.IWeChatApiService;
import pers.hll.aigc4chat.server.service.IWeChatMessageService;

/**
 * 消息控制器
 *
 * @author hll
 * @since 2024/04/14
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/message")
@Tag(name = "MessageController", description = "消息控制器")
public class MessageController {

    private final IWeChatMessageService weChatMessageService;

    private final IWeChatApiService weChatApiService;

    @GetMapping("/page/message")
    public R<IPage<WeChatMessage>> pageMessage(WeChatMessagePageQuery query) {
        return R.data(weChatMessageService.pageMessage(query));
    }

    @GetMapping("/send/text")
    public R<LoginResp> sendText(String text, String toUserName) {
        if (syncCheck()) {
            return R.fail("已断开连接!");
        }
        weChatApiService.sendTextMessage(text, toUserName);
        return R.success();
    }

    private boolean syncCheck() {
        SyncCheckResp syncCheckResp = weChatApiService.syncCheck();
        if (syncCheckResp == null && syncCheckResp.getRetCode() != 0) {
            return false;
        }
        return true;
    }
}
