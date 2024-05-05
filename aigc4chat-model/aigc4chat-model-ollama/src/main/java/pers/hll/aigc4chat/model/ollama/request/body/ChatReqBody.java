package pers.hll.aigc4chat.model.ollama.request.body;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Parameters类，用于封装模型交互所需的参数
 * <pre>{@code
 * curl http://localhost:11434/api/chat -d '{
 *   "model": "llama2",
 *   "messages": [
 *     {
 *       "role": "user",
 *       "content": "why is the sky blue?"
 *     }
 *   ]
 * }
 * }
 *
 * </pre>
 * @author hll
 * @since 2024/05/03
 */
@Data
@Builder
public class ChatReqBody<T> {
  
    /**  
     * 必需的模型名称  
     */  
    private String model;  
  
    /**  
     * 聊天消息，用于保持聊天记忆  
     */  
    private List<Message> messages;
  
    /**  
     * 响应的格式，目前只接受"json"  
     */  
    private String format;
  
    /**  
     * 附加的模型参数，如温度等，在Modelfile文档中列出  
     */  
    private T options;
  
    /**  
     * 如果为false，响应将作为单个响应对象返回，而不是对象流  
     */  
    private Boolean stream;  
  
    /**  
     * 控制模型在请求后保持加载到内存中的时间（默认为5分钟）5m
     */
    @SerializedName("keep_alive")
    private String keepAlive;
}