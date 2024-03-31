package pers.lys.aigc4chat.common.ai.entity.agent;

import lombok.Data;

import java.util.List;

/**
 * Agent自定义对话模型响应参数
 *
 * @author LiYaosheng
 * @since 2024/3/23
 */
@Data
public class AgentResp {

    private Message data;

    @Data
    public static class Message {

        private List<Answer> answer;
    }

    @Data
    public static class Answer {

        private Reply reply;
    }

    @Data
    public static class Reply {

        private String text;
    }
}
