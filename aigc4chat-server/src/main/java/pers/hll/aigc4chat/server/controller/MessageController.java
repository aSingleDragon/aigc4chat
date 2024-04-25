package pers.hll.aigc4chat.server.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pers.hll.aigc4chat.base.util.MultipartFileUtil;
import pers.hll.aigc4chat.entity.wechat.message.OriContent;
import pers.hll.aigc4chat.protocol.wechat.response.LoginResp;
import pers.hll.aigc4chat.protocol.wechat.response.SyncCheckResp;
import pers.hll.aigc4chat.server.base.R;
import pers.hll.aigc4chat.server.bean.WeChatMessagePageQuery;
import pers.hll.aigc4chat.server.entity.WeChatMessage;
import pers.hll.aigc4chat.server.service.IWeChatApiService;
import pers.hll.aigc4chat.server.service.IWeChatMessageService;

import java.io.IOException;

/**
 * 消息控制器
 *
 * @author hll
 * @since 2024/04/14
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/message")
@Tag(name = "MessageController", description = "消息控制器")
public class MessageController {

    private final IWeChatMessageService weChatMessageService;

    private final IWeChatApiService weChatApiService;

    @GetMapping("/page")
    public R<IPage<WeChatMessage>> pageMessage(WeChatMessagePageQuery query) {
        return R.data(weChatMessageService.pageMessage(query));
    }

    @PostMapping("/send/text")
    public R<LoginResp> sendText(@RequestParam String text, @RequestParam String toUserName) {
        syncCheck();
        weChatApiService.sendTextMessage(text, toUserName);
        return R.success();
    }

    @PostMapping("/send/location")
    public R<LoginResp> sendLocation(@RequestBody OriContent location, @RequestParam String toUserName) {
        syncCheck();
        weChatApiService.sendLocationMessage(location, toUserName);
        return R.success();
    }

    @PostMapping(value = "/send/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE )
    public R<LoginResp> sendFile(@RequestPart("file") MultipartFile file, @RequestParam String toUserName) throws IOException {
        syncCheck();
        String filePath = MultipartFileUtil.saveFile(file);
        weChatApiService.sendFileMessage(filePath, toUserName);
        return R.success(filePath);
    }

    private void syncCheck() {
        try {
            SyncCheckResp syncCheckResp = weChatApiService.syncCheck();
            if (syncCheckResp == null || syncCheckResp.getRetCode() != 0) {
                throw new IllegalStateException("同步检查失败");
            }
        } catch (Exception e) {
            log.error("同步检查失败", e);
            throw new IllegalStateException("同步检查失败: ", e);
        }
    }
}
