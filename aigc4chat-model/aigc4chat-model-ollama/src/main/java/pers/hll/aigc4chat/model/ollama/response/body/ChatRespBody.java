package pers.hll.aigc4chat.model.ollama.response.body;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pers.hll.aigc4chat.model.ollama.request.body.Message;

/**
 * 参数类，包含模型交互的响应信息
 *
 * @author hll
 * @since 2024/05/03
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ChatRespBody extends BaseRespBody {
  
    /**
     * 消息对象，包含聊天的内容和其他相关信息
     */
    private Message message;
}