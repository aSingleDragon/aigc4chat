package pers.hll.aigc4chat.server.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger 配置
 *
 * @author hll
 * @since 2024/04/14
 */
@Configuration
public class SwaggerConfig {

    private License license() {
        return new License()
                .name("MIT")
                .url("https://opensource.org/licenses/MIT");
    }

    private Info info(){
        return new Info()
                .title("Aigc4Chat API")
                .description("Aigc4Chat API说明文档。")
                .version("v1.0.0")
                .license(license());
    }

    private ExternalDocumentation externalDocumentation() {
        return new ExternalDocumentation()
                .description("欢迎 Star | Fork | Commit | Issue.")
                .url("https://gitee.com/aSingleDragon/aigc4chat");
    }

    @Bean
    public OpenAPI openApi() {
        return new OpenAPI()
                .info(info())
                .externalDocs(externalDocumentation());
    }
}
