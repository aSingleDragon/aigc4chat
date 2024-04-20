package pers.hll.aigc4chat.server.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import pers.hll.aigc4chat.common.entity.wechat.message.OriContent;
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

    private static final Logger log = LoggerFactory.getLogger(MessageController.class);
    private final IWeChatMessageService weChatMessageService;

    private final IWeChatApiService weChatApiService;

    @GetMapping("/page")
    public R<IPage<WeChatMessage>> pageMessage(WeChatMessagePageQuery query) {
        return R.data(weChatMessageService.pageMessage(query));
    }

    @PostMapping("/send/text")
    public R<LoginResp> sendText(@RequestParam String text, @RequestParam String toUserName) {
        if (!syncCheck()) {
            return R.fail("已断开连接!");
        }
        weChatApiService.sendTextMessage(text, toUserName);
        return R.success();
    }

    @PostMapping("/send/location")
    public R<LoginResp> sendLocation(@RequestBody OriContent location, @RequestParam String toUserName) {
        if (!syncCheck()) {
            return R.fail("已断开连接!");
        }
        weChatApiService.sendLocationMessage(location, toUserName);
        return R.success();
    }

    @PostMapping("/send/file")
    public R<LoginResp> sendFile(HttpServletRequest request, @RequestParam String toUserName) {
        if (!syncCheck()) {
            return R.fail("已断开连接!");
        }
        // TODO 上传文件
        String filePath = "";
        weChatApiService.sendFileMessage(filePath, toUserName);
        return R.success();
    }

    private boolean syncCheck() {
        try {
            SyncCheckResp syncCheckResp = weChatApiService.syncCheck();
            return syncCheckResp != null && syncCheckResp.getRetCode() == 0;
        } catch (Exception e) {
            log.error("同步检查失败", e);
            return false;
        }
    }
}
