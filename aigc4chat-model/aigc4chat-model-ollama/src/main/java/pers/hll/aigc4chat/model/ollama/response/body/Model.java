package pers.hll.aigc4chat.model.ollama.response.body;

import lombok.Data;

/**
 * 模型类，包含模型的详细信息
 *
 * @author hll
 * @since 2024/05/03
 */
@Data
public class Model {
    /**
     * 模型名称
     */
    private String name;

    /**
     * 模型最后修改时间
     */
    private String modifiedAt;

    /**
     * 模型大小（字节）
     */
    private long size;

    /**
     * 模型的摘要（哈希值）
     */
    private String digest;

    /**
     * 模型的详细参数
     */
    private Details details;
}