package pers.hll.aigc4chat.entity.wechat.message;

import jakarta.xml.bind.annotation.*;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * 应用消息
 *
 * @author hll
 * @since 2024/04/09
 */
@Data
@XmlRootElement(name = "appmsg")
@XmlAccessorType(XmlAccessType.FIELD)
public class AppMsg {

    @XmlAttribute(name = "appid")
    private String appId = "wxeb7ec651dd0aefa9";

    @XmlAttribute(name = "sdkver")
    private String sdkVersion = "";

    @XmlElement(name = "title")
    private String title;

    @XmlElement(name = "des")
    private String des;

    @XmlElement(name = "action")
    private String action;

    @XmlElement(name = "type")
    private int type = 6;

    @XmlElement(name = "content")
    private String content;

    @XmlElement(name = "url")
    private String url;

    @XmlElement(name = "lowurl")
    private String lowUrl;

    @XmlElement(name = "appattach")
    private AppAttach appAttach;

    @XmlElement(name = "extinfo")
    private String extInfo;

    @Data
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class AppAttach {
        public AppAttach(long totalLen, String attachId, String fileExt) {
            this.totalLen = totalLen;
            this.attachId = attachId;
            this.fileExt = StringUtils.isEmpty(fileExt) ? "undefined" : fileExt;
        }

        @XmlElement(name = "totallen")
        private long totalLen;
  
        @XmlElement(name = "attachid")
        private String attachId;
  
        @XmlElement(name = "fileext")
        private String fileExt;
    }
}