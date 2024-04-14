package pers.hll.aigc4chat.server.bean;

import lombok.Data;

/**
 * 基础分页查询参数
 *
 * @author hll
 * @since 2024/04/14
 */
@Data
public class PageQuery {

    private Integer pageNum;

    private Integer pageSize;
}
