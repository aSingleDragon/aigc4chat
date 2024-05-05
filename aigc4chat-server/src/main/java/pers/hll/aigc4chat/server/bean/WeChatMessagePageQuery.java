package pers.hll.aigc4chat.server.bean;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 消息分页查询参数
 *
 * @author hll
 * @since 2024/04/14
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WeChatMessagePageQuery extends PageQuery {

    private String formUserName;

    @Parameter(description = "要发送到的用户名")
    private String toUserName;
}
