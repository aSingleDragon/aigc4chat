package pers.hll.aigc4chat.server.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import pers.hll.aigc4chat.base.util.BaseUtil;
import pers.hll.aigc4chat.base.util.XmlUtil;
import pers.hll.aigc4chat.base.xml.XmlConfig;
import pers.hll.aigc4chat.server.bean.vo.XmlConfigVO;
import pers.hll.aigc4chat.server.service.IXmlConfigService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Xml 配置服务接口实现
 *
 * @author hll
 * @since 2024/04/25
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class XmlConfigServiceImpl implements IXmlConfigService {

    private final ApplicationContext applicationContext;

    @Override
    public List<XmlConfigVO<XmlConfig>> getXmlConfigBeanMap() {
        Map<String, XmlConfig> xmlConfigBeanMap = applicationContext.getBeansOfType(XmlConfig.class);
        List<XmlConfigVO<XmlConfig>> xmlConfigVOList = new ArrayList<>();
        for (Map.Entry<String, XmlConfig> entry : xmlConfigBeanMap.entrySet()) {
            Class<? extends XmlConfig> xmlConfigBeanClazz = entry.getValue().getClass();
            String xmlConfigName = XmlUtil.getXmlConfigName(xmlConfigBeanClazz);
            try {
                xmlConfigVOList.add(XmlConfigVO.builder()
                        .fileName(xmlConfigName)
                        .filePath(XmlUtil.getXmlConfigFilePath(xmlConfigName))
                        .config(XmlUtil.readXmlConfig(xmlConfigBeanClazz))
                        .build());
            } catch (IOException e) {
                log.error("读取配置文件失败", e);
            }
        }
        return xmlConfigVOList;
    }

    @Override
    public XmlConfigVO<XmlConfig> getByFileName(String fileName) {
        Map<String, XmlConfig> xmlConfigBeanMap = applicationContext.getBeansOfType(XmlConfig.class);
        Class<? extends XmlConfig> xmlConfigBeanClazz = xmlConfigBeanMap.get(fileName).getClass();
        XmlConfigVO<XmlConfig> xmlConfigVO = null;
        try {
            xmlConfigVO = XmlConfigVO.builder()
                    .fileName(fileName)
                    .filePath(XmlUtil.getXmlConfigFilePath(fileName))
                    .config(XmlUtil.readXmlConfig(xmlConfigBeanClazz))
                    .build();
        } catch (IOException e) {
            log.error("读取[{}]配置文件失败", fileName, e);
        }
        return xmlConfigVO;
    }

    @Override
    public void saveOrUpdate(Object config, String fileName) {
        Map<String, XmlConfig> xmlConfigBeanMap = applicationContext.getBeansOfType(XmlConfig.class);
        Class<? extends XmlConfig> xmlConfigBeanClazz = xmlConfigBeanMap.get(fileName).getClass();
        XmlConfig xmlConfig = BaseUtil.GSON.fromJson(BaseUtil.GSON.toJson(config), xmlConfigBeanClazz);
        try {
            XmlUtil.writeXmlConfig(xmlConfig);
        } catch (IOException e) {
            log.error("写入XML配置异常: ", e);
        }
    }
}
