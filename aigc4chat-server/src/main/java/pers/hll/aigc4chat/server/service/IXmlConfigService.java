package pers.hll.aigc4chat.server.service;

import pers.hll.aigc4chat.base.xml.XmlConfig;
import pers.hll.aigc4chat.server.bean.vo.XmlConfigVO;

import java.util.List;

/**
 * Xml 配置服务接口
 *
 * @author hll
 * @since 2024/04/25
 */
public interface IXmlConfigService {

    List<XmlConfigVO<XmlConfig>> getXmlConfigBeanMap();

    XmlConfigVO<XmlConfig> getByFileName(String fileName);

    void saveOrUpdate(Object config, String fileName);
}
