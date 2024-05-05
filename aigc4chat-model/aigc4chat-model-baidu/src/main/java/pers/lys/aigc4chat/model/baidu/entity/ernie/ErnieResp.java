package pers.lys.aigc4chat.model.baidu.entity.ernie;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * ERNIE-Lite-8K-0922 模型对话请求 Reps
 *
 * @author LiYaosheng
 * @since 2024/3/23
 */
@Data
public class ErnieResp {
    /**
     * 本轮对话的id
     */
    private String id;

    /**
     * 回包类型
     * chat.completion：多轮对话返回
     */
    private String object;

    /**
     * 时间戳
     */
    private int created;

    /**
     * 表示当前子句的序号。只有在流式接口模式下会返回该字段
     */
    @SerializedName("sentence_id")
    private int sentenceId;

    /**
     * 表示当前子句是否是最后一句。只有在流式接口模式下会返回该字段
     */
    @SerializedName("is_end")
    private boolean isEnd;

    /**
     * 当前生成的结果是否被截断
     */
    @SerializedName("is_truncated")
    private boolean isTruncated;

    /**
     * 对话返回结果
     */
    private String result;

    /**
     * 表示用户输入是否存在安全，是否关闭当前会话，清理历史会话信息。
     * true：是，表示用户输入存在安全风险，建议关闭当前会话，清理历史会话信息。
     * false：否，表示用户输入无安全风险
     */
    @SerializedName("need_clear_history")
    private boolean needClearHistory;

    /**
     * 当need_clear_history为true时，此字段会告知第几轮对话有敏感信息，如果是当前问题，ban_round=-1
     */
    @SerializedName("ban_round")
    private int banRound;

    /**
     * token统计信息
     */
    private Usage usage;

    /**
     * token 信息统计
     */
    @Data
    public static class Usage {

        /**
         * 问题tokens数
         */
        @SerializedName("prompt_tokens")
        private int promptTokens;

        /**
         * 回答tokens数
         */
        @SerializedName("completion_tokens")
        private int completionTokens;

        /**
         * tokens总数
         */
        @SerializedName("total_tokens")
        private int totalTokens;
    }
}
