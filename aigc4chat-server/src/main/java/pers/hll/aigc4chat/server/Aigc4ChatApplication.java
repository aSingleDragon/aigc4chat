package pers.hll.aigc4chat.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 启动类
 *
 * @author hll
 * @since 2024/03/09
 */
@EnableAsync
@MapperScan(value = "pers.hll.aigc4chat.server.mapper")
@SpringBootApplication(scanBasePackages = "pers.hll.aigc4chat.*")
public class Aigc4ChatApplication {

    public static void main(String[] args) {
        // Spring应用默认是以无头(headless)模式运行的, 即不支持图形化界面的操作
        // 如果不关闭这个设置 那么 QRCodeUtil.writeInImageAndOpen() 方法会报错
        System.setProperty("java.awt.headless", "false");
        SpringApplication.run(Aigc4ChatApplication.class, args);
    }
}
