package pers.hll.aigc4chat.protocol.wechat.response;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;


/**
 * 响应消息体
 * <blockquote><pre>{@code
 * <error>
 *   <ret>0</ret>
 *   <message></message>
 *   <skey>@crypt_8b7318fd_d8819ef6043a3e5eaf24b89e09f0bdc0</skey>
 *   <wxsid>PNWg3LQayQU8fCCG</wxsid>
 *   <wxuin>2977348135</wxuin>
 *   <pass_ticket>XpIfrzRRUnnmFUWq0I%2FT...jEHhDL28hw%3D%3D</pass_ticket>
 *   <isgrayscale>1</isgrayscale>
 * </error>
 * }
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
