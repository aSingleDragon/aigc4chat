package pers.hll.aigc4chat.server.config.mp;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 填充公共字段
 *
 * @author hll
 * @since 2024/03/27
 */
@Slf4j
@Component
public class Aigc4ChatMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("start insert fill...");
        LocalDateTime now = LocalDateTime.now();
        this.setFieldValByName("createdTime", now, metaObject);
        this.setFieldValByName("updatedTime", now, metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("start update fill...");
        this.setFieldValByName("updatedTime", LocalDateTime.now(), metaObject);
    }
}
