package pers.hll.aigc4chat.server.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Jackson全局配置
 *
 * @author hll
 * @since 2024/04/20
 */
@Configuration
public class JacksonConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer addModules() {
        return builder -> builder.serializationInclusion(JsonInclude.Include.NON_NULL);
    }
}