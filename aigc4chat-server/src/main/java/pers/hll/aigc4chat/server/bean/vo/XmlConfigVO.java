package pers.hll.aigc4chat.server.bean.vo;

import lombok.Builder;
import lombok.Data;
import pers.hll.aigc4chat.base.xml.XmlConfig;

/**
 * 配置文件VO
 *
 * @author hll
 * @since 2024/04/25
 */
@Data
@Builder
public class XmlConfigVO<T extends XmlConfig> {

    private String fileName;

    private String filePath;

    private T config;
}
