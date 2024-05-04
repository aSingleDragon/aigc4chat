package pers.hll.aigc4chat.model.ollama.response.body;

import lombok.Data;

import java.util.List;

/**
 * 模型的详细参数类
 *
 * @author hll
 * @since 2024/05/03
 * @see Model
 * @see ShowRespBody
 * @see TagsRespBody
 */
@Data
public class Details {

    /**
     * 模型格式
     */
    private String format;

    /**
     * 模型家族
     */
    private String family;

    /**
     * 模型的家族列表（可能为空）
     */
    private List<String> families;

    /**
     * 模型的参数大小
     */
    private String parameterSize;

    /**
     * 模型的量化级别
     */
    private String quantizationLevel;
}