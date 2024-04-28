package pers.hll.aigc4chat.base.xml;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import pers.hll.aigc4chat.base.util.XmlUtil;

/**
 * 代理服务器xml配置
 * <p>{@link Data} Json序列化时用到其生成的 Getter Setter 方法
 * <p>{@link JsonInclude} 全局配置的null值不序列化 假如暂时没有配置类对应的xml配置文件 我们需要查看xml配置格式 此时null值就需要序列化
 * <p>{@link NoArgsConstructor} {@link XmlUtil#objectToXmlStr}里的{@link JAXBContext#newInstance(Class[])}用到
 * <p>{@link AllArgsConstructor} 生成实例对象时用到 直接用 new 或者 {@link Builder} 生成也可以
 * 此时 {@link NoArgsConstructor} 就不再需要了
 * <p>{@link XmlAccessorType} Xml序列化时需要
 * <p>{@link Component} 配置类交给Spring管理 需要配合 {@link XmlConfig} 一起使用
 * <p>{@link XmlRootElement} Xml序列化时需要 没有这个注解 读取配置类会报错
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