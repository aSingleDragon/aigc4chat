package pers.hll.aigc4chat.model.ollama.response.body;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 调用 /api/* 接口的基础响应体
 *
 * @author hll
 * @since 2024/04/29
 */
@Data
public class BaseRespBody {

    /**
     * 模型名称
     */
    private String model;

    /**
     * 创建时间
     */
    @SerializedName("created_at")
    private LocalDateTime createdAt;

    /**
     * 是否完成 stream模式下最后一条数据 done 为 true
     */
    private Boolean done;

    /**
     * 生成本次响应所消耗的总时间（毫秒单位）
     */
    @SerializedName("total_duration")
    private Long totalDuration;

    /**
     * 加载模型所消耗的时间（纳秒单位）
     */
    @SerializedName("load_duration")
    private Long loadDuration;

    /**
     * 提示符中的令牌数量
     */
    @SerializedName("prompt_eval_count")
    private Integer promptEvalCount;

    /**
     * 评估提示符所消耗的时间（纳秒单位）
     */
    @SerializedName("prompt_eval_duration")
    private Long promptEvalDuration;

    /**
     * 响应中的令牌数量
     */
    @SerializedName("eval_count")
    private Integer evalCount;

    /**
     * 生成响应内容所消耗的时间（纳秒单位）
     */
    @SerializedName("eval_duration")
    private Long evalDuration;
}