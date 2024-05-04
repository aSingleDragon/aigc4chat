package pers.hll.aigc4chat.model.ollama.request.body;

import lombok.Builder;
import lombok.Data;
import pers.hll.aigc4chat.model.ollama.constant.Role;

import java.util.List;

/**
 * Message类，用于封装消息对象的字段
 *
 * @author hll
 * @since 2024/05/03
 */
@Data
@Builder
public class Message {

    /**
     * 消息的角色
     */
    private Role role;

    /**
     * 消息的内容
     */
    private String content;

    /**
     * 可选的，消息中包含的图像列表（用于多模态模型，如llava）
     * 这里假设是图像的URL或标识符的列表
     */
    private List<String> images;
}