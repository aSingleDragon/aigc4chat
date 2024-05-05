package pers.hll.aigc4chat.server.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pers.hll.aigc4chat.model.ollama.OllamaApi;
import pers.hll.aigc4chat.model.ollama.request.body.ChatReqBody;
import pers.hll.aigc4chat.model.ollama.request.body.GenerateReqBody;
import pers.hll.aigc4chat.model.ollama.response.body.ChatRespBody;
import pers.hll.aigc4chat.model.ollama.response.body.GenerateRespBody;
import pers.hll.aigc4chat.model.ollama.response.body.ShowRespBody;
import pers.hll.aigc4chat.model.ollama.response.body.TagsRespBody;
import pers.hll.aigc4chat.server.base.R;

/**
 * Ollama控制器
 *
 * @author hll
 * @since 2024/05/03
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/ollama")
@Tag(name = "OllamaController", description = "Ollama控制器")
public class OllamaController {

    @PostMapping("/generate")
    @Operation(summary = "生成")
    public R<GenerateRespBody> generate(@RequestBody GenerateReqBody<?> generateReqBody) {
        return R.data(OllamaApi.generate(generateReqBody));
    }

    @PostMapping("/easy-generate")
    @Operation(summary = "对话", description = "默认使用llama2模型")
    public R<GenerateRespBody> easyGenerate(@RequestParam @Parameter(description = "要发送的文字内容") String content) {
        return R.data(OllamaApi.easyGenerate(content));
    }

    @PostMapping("/chat")
    @Operation(summary = "对话")
    public R<ChatRespBody> chat(@RequestBody ChatReqBody<?> chatReqBody) {
        return R.data(OllamaApi.chat(chatReqBody));
    }

    @PostMapping("/easy-chat")
    @Operation(summary = "对话", description = "默认以user身份使用llama2模型")
    public R<ChatRespBody> easyChat(@RequestParam @Parameter(description = "要发送的文字内容") String content) {
        return R.data(OllamaApi.easyChat(content));
    }

    @PostMapping("/show")
    @Operation(summary = "模型详情")
    public R<ShowRespBody> show(@RequestParam @Parameter(description = "模型名称") String model) {
        return R.data(OllamaApi.show(model));
    }

    @GetMapping("/tags")
    @Operation(summary = "本地模型列表")
    public R<TagsRespBody> tags() {
        return R.data(OllamaApi.tags());
    }
}
