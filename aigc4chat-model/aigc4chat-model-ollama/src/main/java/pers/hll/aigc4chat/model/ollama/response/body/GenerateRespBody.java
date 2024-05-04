package pers.hll.aigc4chat.model.ollama.response.body;

import lombok.Data;

import java.util.List;

/**
 * 调用 /api/generate 接口的响应体
 *
 * @author hll
 * @since 2024/04/29
 */
@Data
public class GenerateRespBody extends BaseRespBody {

    /**
     * 对话会话的编码，可用于保持对话记忆，可以将其发送至下一次请求中
     */
    private List<Integer> context;

    /**
     * 如果响应未被流式处理，此处将包含完整响应内容；
     * 若已采用流式处理，则为空
     */
    private String response;
}