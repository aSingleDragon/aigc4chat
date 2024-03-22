package pers.hll.aigc4chat.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动类
 *
 * @author hll
 * @since 2024/03/09
 */
@SpringBootApplication
@MapperScan(value = "pers.hll.aigc4chat.server.mapper")
public class Aigc4ChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(Aigc4ChatApplication.class, args);
    }
}
