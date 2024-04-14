package pers.hll.aigc4chat.server.bean;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.Data;

/**
 * 用户分页查询参数
 *
 * @author hll
 * @since 2024/04/14
 */
@Data
public class WeChatUserPageQuery extends PageQuery {

    @Parameter(name = "userName", description = "用户名")
    private String userName;
}
