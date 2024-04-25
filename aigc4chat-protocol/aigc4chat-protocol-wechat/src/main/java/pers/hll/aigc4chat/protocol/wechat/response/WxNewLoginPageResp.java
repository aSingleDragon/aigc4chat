package pers.hll.aigc4chat.protocol.wechat.response;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;


/**
 * 响应消息体
 * <blockquote><pre>
 * &lt;error&gt;
 *   &lt;ret&gt;0&lt;/ret&gt;
 *   &lt;message&gt;&lt;/message&gt;
 *   &lt;skey&gt;@crypt_8b7318fd_d8819ef6043a3e5eaf24b89e09f0bdc0&lt;/skey&gt;
 *   &lt;wxsid&gt;PNWg3LQayQU8fCCG&lt;/wxsid&gt;
 *   &lt;wxuin&gt;2977348135&lt;/wxuin&gt;
 *   &lt;pass_ticket&gt;XpIfrzRRUnnmFUWq0I%2FT...jEHhDL28hw%3D%3D&lt;/pass_ticket&gt;
 *   &lt;isgrayscale&gt;1&lt;/isgrayscale&gt;
 * &lt;/error&gt;
 * </pre></blockquote>
 *
 * @author hll
 * @since 2024/03/12
 */
@Data
@XmlRootElement(name = "error")
@XmlAccessorType(XmlAccessType.FIELD)
public class WxNewLoginPageResp {

    @XmlElement(name="ret")
    private String ret;

    @XmlElement(name="message")
    private String message;

    @XmlElement(name="skey")
    private String sKey;

    @XmlElement(name="wxsid")
    private String wxSid;

    @XmlElement(name="wxuin")
    private String wxUin;

    @XmlElement(name="pass_ticket")
    private String passTicket;

    @XmlElement(name="isgrayscale")
    private String isGrayScale;
}
