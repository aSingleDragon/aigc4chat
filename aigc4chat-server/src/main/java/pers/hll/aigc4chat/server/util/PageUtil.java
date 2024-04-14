package pers.hll.aigc4chat.server.util;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.experimental.UtilityClass;
import pers.hll.aigc4chat.server.bean.PageQuery;

/**
 * 分页工具类
 *
 * @author hll
 * @since 2024/04/14
 */
@UtilityClass
public class PageUtil {

    public <T> IPage<T> createPage(PageQuery query) {
        int pageNum = null == query.getPageNum() || 0 == query.getPageNum() ? 1 : query.getPageNum();
        int pageSize = null == query.getPageSize() || 0 == query.getPageSize() ? 10 : query.getPageSize();
        return new Page<>(pageNum, pageSize);
    }
}
