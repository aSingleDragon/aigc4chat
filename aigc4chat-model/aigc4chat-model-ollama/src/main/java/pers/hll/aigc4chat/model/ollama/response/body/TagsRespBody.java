package pers.hll.aigc4chat.model.ollama.response.body;

import lombok.Data;
  
import java.util.List;  
  
/**  
 * 模型集合类，包含多个模型的信息
 *
 * @author hll
 * @since 2024/05/03
 */  
@Data  
public class TagsRespBody {
  
    /**  
     * 模型列表  
     */  
    private List<Model> models;
}