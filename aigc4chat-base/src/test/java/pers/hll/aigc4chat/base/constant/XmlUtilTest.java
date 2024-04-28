package pers.hll.aigc4chat.base.constant;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import pers.hll.aigc4chat.base.util.XmlUtil;
import pers.hll.aigc4chat.base.xml.ProxyConfig;

import java.io.IOException;

/**
 * 随便写的测试类 不要在意
 *
 * @author hll
 * @since 2024/04/26
 */
@Slf4j
@SpringBootTest
public class XmlUtilTest {

    @Test
    public void testWriteXmlConfig() throws IOException {
        ProxyConfig proxyConfig = new ProxyConfig("127.0.0.1", 1080, "hll", "123");
        XmlUtil.writeXmlConfig(proxyConfig);
    }
}