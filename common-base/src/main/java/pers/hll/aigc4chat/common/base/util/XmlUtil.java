package pers.hll.aigc4chat.common.base.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

/**
 * Xml解析工具类
 *
 * @author hll
 * @since 2023/03/11
 */
@Slf4j
@UtilityClass
public class XmlUtil {

    public void parse(String content) throws DocumentException {
        Document document = DocumentHelper.parseText(content);
        // 获取根节点,在例子中就是responsedata节点
        Element rootElement = document.getRootElement();
        // 获取根节点下的某个元素
        Element resultcode = rootElement.element("ret");
        Element message = rootElement.element("message");
        Element skey = rootElement.element("skey");
        Element wxsid = rootElement.element("wxsid");
        Element wxuin = rootElement.element("wxuin");
        Element pass_ticket = rootElement.element("pass_ticket");
        Element isgrayscale = rootElement.element("isgrayscale");
        System.out.println(resultcode.getData());
    }

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
            // 进行将Xml转成对象的核心接口
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
