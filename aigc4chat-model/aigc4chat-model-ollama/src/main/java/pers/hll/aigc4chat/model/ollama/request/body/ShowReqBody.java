package pers.hll.aigc4chat.model.ollama.request.body;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 模型详情接口请求体
 * <pre>{@code
 *
 * </pre>
 * @author hll
 * @since 2024/05/03
 */
@Data
@AllArgsConstructor
public class ShowReqBody {
  
    /**  
     * 必需的模型名称  
     */  
    private String name;
}