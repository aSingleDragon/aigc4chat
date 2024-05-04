package pers.hll.aigc4chat.server.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pers.hll.aigc4chat.model.ollama.OllamaApi;
import pers.hll.aigc4chat.model.ollama.constant.ModelName;
import pers.hll.aigc4chat.model.ollama.constant.Role;
import pers.hll.aigc4chat.model.ollama.request.body.ChatReqBody;
import pers.hll.aigc4chat.model.ollama.request.body.Message;
import pers.hll.aigc4chat.model.ollama.response.body.ChatRespBody;
import pers.hll.aigc4chat.protocol.wechat.response.webwxsync.AddMsg;
import pers.hll.aigc4chat.server.service.IWeChatApiService;

import java.util.Collections;

/**
 * 消息处理器
 *
 * @author hll
 * @since 2024/04/21
 */
@RequiredArgsConstructor
@Component(MessageHandlerName.OLLAMA_MESSAGE_HANDLER)
public class OllamaMessageHandler implements MessageHandler {

    private final IWeChatApiService weChatApiService;

    @Override
    public void handle(AddMsg addMsg) {
        ChatRespBody chatRespBody = OllamaApi.chat(ChatReqBody.builder()
                .model(ModelName.LLAMA2)
                .messages(Collections.singletonList(Message.builder()
                        .role(Role.USER)
                        .content(addMsg.getContent())
                        .build()))
                .stream(false)
                .build());
        weChatApiService.sendTextMessage(chatRespBody.getMessage().getContent(), addMsg.getFromUserName());
    }
}
