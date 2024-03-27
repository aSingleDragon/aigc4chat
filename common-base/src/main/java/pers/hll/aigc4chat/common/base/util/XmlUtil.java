package pers.hll.aigc4chat.common.base.util;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.StringReader;
import java.io.StringWriter;
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
    public <T> T xmlStrToObject(String xmlStr, Class<T> clazz) {
        T xmlObject = null;
        try {
            JAXBContext context = JAXBContext.newInstance(clazz);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            StringReader sr = new StringReader(xmlStr);
            xmlObject = clazz.cast(unmarshaller.unmarshal(sr));
        } catch (JAXBException e) {
            log.error("XML字符串:\n{}\n转换为[{}]实例对象失败: ", xmlStr, clazz.getName(), e);
        }
        return xmlObject;
    }

    /**
     * 对象转XML字符串
     *
     * @param obj 实例对象
     * @param clazz 类
     * @return XML字符串
     */
    public <T> String objectToXmlStr(T obj, Class<T> clazz) {
        String result = "";
        try {
            JAXBContext context = JAXBContext.newInstance(clazz);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, StandardCharsets.UTF_8.name());
            StringWriter writer = new StringWriter();
            marshaller.marshal(obj, writer);
            result = writer.toString();
        } catch (Exception e) {
            log.error("实例对象[{}]转换XML字符串失败: ", clazz.getName(), e);
        }
        return result;
    }
}
