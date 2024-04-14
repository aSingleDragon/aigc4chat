package pers.hll.aigc4chat.server.bean;

import lombok.Data;

/**
 * 消息分页查询参数
 *
 * @author hll
 * @since 2024/04/14
 */
@Data
public class WeChatMessagePageQuery extends PageQuery {

    private String formUserName;

    private String toUserName;
}
