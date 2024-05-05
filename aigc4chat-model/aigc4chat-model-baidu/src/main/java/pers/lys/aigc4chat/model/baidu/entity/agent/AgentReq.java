package pers.lys.aigc4chat.model.baidu.entity.agent;

import lombok.Data;

/**
 * Agent自定义对话模型请求参数
 *
 * @author LiYaosheng
 * @since 2024/3/23
 */
@Data
public class AgentReq {

    /**
     * 用户问题
     */
    private String queryText;
}
