package pers.hll.aigc4chat.base.util;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.AnnotationConfigurationException;
import org.springframework.core.annotation.AnnotationUtils;
import pers.hll.aigc4chat.base.constant.FilePath;
import pers.hll.aigc4chat.base.exception.BizException;
import pers.hll.aigc4chat.base.xml.XmlConfig;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Xml解析工具类
 *
 * @author hll
 * @since 2024/03/11
 */
@Slf4j
@UtilityClass
public class XmlUtil {

    /**
     * xml字符串转对象
     *
     * @param xmlStr XML 字符穿
     * @param clazz  要转换的类
     * @return 转换后的类的实例对象
     */
    public <T> T xmlStrToObject(String xmlStr, Class<T> clazz)  {
        T xmlObject = null;
        if (StringUtils.isBlank(xmlStr)) {
            try {
                xmlObject = clazz.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                log.error("实例化类[{}]失败: ", clazz.getName(), e);
                throw BizException.of("实例化类[{}]失败", clazz.getName(), e);
            }
            return xmlObject;
        }
        try {
            JAXBContext context = JAXBContext.newInstance(clazz);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            StringReader sr = new StringReader(xmlStr);
            xmlObject = clazz.cast(unmarshaller.unmarshal(sr));
        } catch (Exception e) {
            log.error("XML字符串:\n{}\n转换为[{}]实例对象失败: ", xmlStr, clazz.getName(), e);
            throw BizException.of("XML字符串:\n{}\n转换为[{}]实例对象失败", xmlStr, clazz.getName(), e);
        }
        return xmlObject;
    }

    /**
     * 对象转XML字符串
     *
     * @param obj 实例对象
     * @return XML字符串
     */
    public <T> String objectToXmlStr(T obj) {
        String result = "";
        try {
            JAXBContext context = JAXBContext.newInstance(obj.getClass());
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, StandardCharsets.UTF_8.name());
            StringWriter writer = new StringWriter();
            marshaller.marshal(obj, writer);
            result = writer.toString();
        } catch (Exception e) {
            log.error("实例对象[{}]转换XML字符串失败: ", obj.getClass(), e);
            throw BizException.of("实例对象[{}]转换XML字符串失败", obj.getClass(), e);
        }
        return result;
    }

    /**
     * 将{@code obj}写为XML配置文件
     * <p>如果{@code obj}没有{@code @XmlRootElement}注解, 将抛出{@link AnnotationConfigurationException}异常
     * <p>文件名为{@code obj}上的注解{@link XmlRootElement#name()}的值
     *
     * @param obj 实例对象
     * @throws IOException IO异常
     */
    public <T extends XmlConfig> void writeXmlConfig(T obj) throws IOException {
        FileUtils.write(
                new File(getXmlConfigFilePath(getXmlConfigName(obj.getClass()))),
                objectToXmlStr(obj),
                StandardCharsets.UTF_8);
    }

    /**
     * 将XML配置文件转换为{@code clazz}的实例对象
     * <p>如果{@code clazz}没有{@code @XmlRootElement}注解, 将抛出{@link AnnotationConfigurationException}异常
     * 文件名为{@code clazz}上的注解{@link XmlRootElement#name()}的值。
     * 如果文件不存在, 抛出{@link FileNotFoundException}异常</p>
     *
     * @param clazz 要转换的目标类
     * @throws IOException IO异常
     */
    public <T extends XmlConfig> T readXmlConfig(Class<T> clazz) throws IOException {
        File xmlConfigFile = new File(getXmlConfigFilePath(getXmlConfigName(clazz)));
        if (!xmlConfigFile.exists()) {
            log.error("XML配置文件[{}]不存在!", xmlConfigFile.getAbsolutePath());
            throw BizException.of("XML配置文件[{}]不存在!", xmlConfigFile.getAbsolutePath());
        }
        return xmlStrToObject(FileUtils.readFileToString(xmlConfigFile, StandardCharsets.UTF_8), clazz);
    }

    /**
     * 获取XML配置文件路径
     *
     * @param xmlConfigName 配置文件名
     * @return 配置文件路径
     */
    public String getXmlConfigFilePath(String xmlConfigName) {
        return FilePath.XML_CONFIG + xmlConfigName + ".xml";
    }

    /**
     * 获取XML配置文件名
     *
     * @param clazz Xml配置类
     * @return 配置文件名 {@code clazz}上的注解{@link XmlRootElement#name()}的值
     */
    public <T extends XmlConfig> String getXmlConfigName(Class<T> clazz) {
        XmlRootElement root = AnnotationUtils.findAnnotation(clazz, XmlRootElement.class);
        if (root == null || StringUtils.isEmpty(root.name())) {
            log.error("类[{}]缺少 @XmlRootElement 注解!", clazz.getName());
            throw BizException.of("类[{}]缺少 @XmlRootElement 注解!", clazz.getName());
        }
        return root.name();
    }
}
