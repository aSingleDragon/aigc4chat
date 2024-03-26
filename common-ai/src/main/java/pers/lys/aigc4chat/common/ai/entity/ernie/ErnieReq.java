package pers.lys.aigc4chat.common.ai.entity.ernie;

import lombok.Data;

import java.util.List;

/**
 * ERNIE-Lite-8K-0922 模型对话请求 Req
 *
 * @author LiYaosheng
 * @since 2024/3/22
 */
@Data
public class ErnieReq {

    /**
     * 聊天上下文信息
     * 说明：
     * （1）messages成员不能为空，1个成员表示单轮对话，多个成员表示多轮对话
     * （2）最后一个message为当前请求的信息，前面的message为历史对话信息
     * （3）必须为奇数个成员，成员中message的role必须依次为user、assistant
     * （4）message中的content总长度和system字段总内容不能超过11200个字符，且不能超过7168 tokens
     */
    private List<Message> messages;

    /**
     * 会话信息
     */
    @Data
    public static class Message {
        /**
         * 角色   user-用户；assistant-对话助手
         * 默认为user
         */
        String role = "user";

        /**
         * 对话内容
         */
        String content;
    }
}
