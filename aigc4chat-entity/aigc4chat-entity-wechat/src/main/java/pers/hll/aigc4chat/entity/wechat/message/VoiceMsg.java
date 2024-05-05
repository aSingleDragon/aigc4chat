package pers.hll.aigc4chat.entity.wechat.message;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * <a href="https://blog.csdn.net/weixin_44504146/article/details/101457777">🦐🐔8️⃣</a>
 *  实验中 用不了
 *
 * @author hll
 * @since 2024/03/26
 */
@Data
@XmlRootElement(name = "msg")
@XmlAccessorType(XmlAccessType.FIELD)
public class VoiceMsg {

    @XmlElement(name = "ToUserName")
    private String toUserName;

    @XmlElement(name = "FromUserName")
    private String fromUserName;

    @XmlElement(name = "CreateTime")
    private long createTime;

    @XmlElement(name = "MsgType")
    private String msgType;

    @XmlElement(name = "Voice")
    private Voice voice;

    @Data
    @AllArgsConstructor
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Voice {

        @XmlElement(name = "MediaId")
        private String mediaId;
    }
}
