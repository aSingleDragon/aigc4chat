package pers.hll.aigc4chat.common.protocol.wechat.protocol.response;

import lombok.Data;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 *
 * @author hll
 * @since 2023/03/12
 */
@Data
@XmlRootElement(name="error")
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
