package pers.hll.aigc4chat.server.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 基础实体类
 *
 * @author hll
 * @since 2024/04/06
 */
@Data
public class BaseEntity implements Serializable {

    /**
     * 创建时间
     * <a href="https://www.sqlite.org/datatype3.html">Datatypes In SQLite</a>
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;
}
