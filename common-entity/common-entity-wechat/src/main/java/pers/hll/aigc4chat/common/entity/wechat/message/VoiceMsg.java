package pers.hll.aigc4chat.common.entity.wechat.message;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

/**
 * <a href="https://blog.csdn.net/weixin_44504146/article/details/101457777">🦐🐔8️⃣</a>
 *
 * @author hll
 * @since 2024/03/26
 */
@Data
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class VoiceMsg {

    @XmlElement(name = "ToUserName")
    private String toUserName;

    @XmlElement(name = "FromUserName")
    private String fromUserName;

    @XmlElement(name = "CreateTime")
    private String createTime;

    @XmlElement(name = "MsgType")
    private String msgType;

    @XmlElement(name = "Voice")
    private Voice voice;

    @Data
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Voice {

        @XmlElement(name = "MediaId")
        private String mediaId;
    }
}
