package pers.hll.aigc4chat.server.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户分页查询参数
 *
 * @author hll
 * @since 2024/04/14
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WeChatUserGroupPageQuery extends PageQuery {

    private String userName;
}
