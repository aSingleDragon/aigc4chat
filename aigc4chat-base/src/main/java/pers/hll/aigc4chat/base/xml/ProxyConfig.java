package pers.hll.aigc4chat.base.xml;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 代理服务器配置
 *
 * @author hll
 * @since 2024/04/24
 */
@Data
@JsonInclude
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@Component(XmlConfigName.PROXY_CONFIG)
@XmlRootElement(name = XmlConfigName.PROXY_CONFIG)
public class ProxyConfig implements XmlConfig {

    private String host;

    private Integer port;

    private String username;

    private String password;
}