package pers.hll.aigc4chat.model.ollama;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import pers.hll.aigc4chat.model.ollama.constant.EndPoint;
import pers.hll.aigc4chat.model.ollama.constant.Role;
import pers.hll.aigc4chat.model.ollama.constant.ModelName;
import pers.hll.aigc4chat.model.ollama.request.*;
import pers.hll.aigc4chat.model.ollama.request.body.ChatReqBody;
import pers.hll.aigc4chat.model.ollama.request.body.GenerateReqBody;
import pers.hll.aigc4chat.model.ollama.request.body.Message;
import pers.hll.aigc4chat.model.ollama.request.body.ShowReqBody;
import pers.hll.aigc4chat.model.ollama.response.body.ChatRespBody;
import pers.hll.aigc4chat.model.ollama.response.body.GenerateRespBody;
import pers.hll.aigc4chat.model.ollama.response.body.ShowRespBody;
import pers.hll.aigc4chat.model.ollama.response.body.TagsRespBody;

import java.util.Collections;

/**
 * Ollama API
 *
 * @author hll
 * @since 2024/04/30
 */
@Slf4j
@UtilityClass
public final class OllamaApi {

    public GenerateRespBody generate(GenerateReqBody<?> generateReqBody) {
        return OllamaHttpClient.post(new GenerateReq(EndPoint.GENERATE)
                .setRequestBody(generateReqBody));
    }

    public GenerateRespBody easyGenerate(String content) {
        return OllamaApi.generate(GenerateReqBody.builder()
                .model(ModelName.LLAMA2)
                .prompt(content)
                .build());
    }
    public ChatRespBody chat(ChatReqBody<?> chatReqBody) {
        return OllamaHttpClient.post(new ChatReq(EndPoint.CHAT)
                .setRequestBody(chatReqBody));
    }

    public ChatRespBody easyChat(String content) {
        return OllamaApi.chat(ChatReqBody.builder()
                .model(ModelName.LLAMA2)
                .messages(Collections.singletonList(Message.builder()
                        .role(Role.USER)
                        .content(content)
                        .build()))
                .stream(false)
                .build());
    }

    public TagsRespBody tags() {
        return OllamaHttpClient.get(new TagsReq(EndPoint.TAGS));
    }

    public ShowRespBody show(String modelName) {
        return OllamaHttpClient.post(new ShowReq(EndPoint.SHOW)
                .setRequestBody(new ShowReqBody(modelName)));
    }
}
