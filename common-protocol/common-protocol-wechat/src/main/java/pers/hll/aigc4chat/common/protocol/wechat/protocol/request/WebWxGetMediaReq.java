package pers.hll.aigc4chat.common.protocol.wechat.protocol.request;

import lombok.EqualsAndHashCode;
import pers.hll.aigc4chat.common.base.constant.ContentType;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.constant.DefaultConfig;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.constant.WXHeaderKey;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.constant.WXQueryKey;

import java.util.Map;

/**
 * 获取媒体文件请求
 *
 * @author hll
 * @since 2023/03/11
 */
@EqualsAndHashCode(callSuper = true)
public class WebWxGetMediaReq extends BaseRequest<WebWxGetMediaReq, Object> {

    private long msgId;

    private String sKey;

    private String passTicket;

    private String encryFileName;

    private String fromUser;

    private String mediaId;

    private String sender;

    private String webWxDataTicket;

    public WebWxGetMediaReq(String uri) {
        super(uri);
    }

    public WebWxGetMediaReq setMsgId(long msgId) {
        this.msgId = msgId;
        return this;
    }

    public WebWxGetMediaReq setSKey(String sKey) {
        this.sKey = sKey;
        return this;
    }

    public WebWxGetMediaReq setPassTicket(String passTicket) {
        this.passTicket = passTicket;
        return this;
    }

    public WebWxGetMediaReq setEncryFileName(String encryFileName) {
        this.encryFileName = encryFileName;
        return this;
    }

    public WebWxGetMediaReq setFromUser(String fromUser) {
        this.fromUser = fromUser;
        return this;
    }

    public WebWxGetMediaReq setMediaId(String mediaId) {
        this.mediaId = mediaId;
        return this;
    }

    public WebWxGetMediaReq setSender(String sender) {
        this.sender = sender;
        return this;
    }

    public WebWxGetMediaReq setWebWxDataTicket(String webWxDataTicket) {
        this.webWxDataTicket = webWxDataTicket;
        return this;
    }

    @Override
    public WebWxGetMediaReq build() {

        Map<String, String> headerMap = getHeaderMap();
        headerMap.put(WXHeaderKey.USER_AGENT, DefaultConfig.USER_AGENT);
        headerMap.put(WXHeaderKey.CONTENT_TYPE, ContentType.JSON);

        Map<String, Object> requestParamMap = getRequestParamMap();

        requestParamMap.put(WXQueryKey.ENCRY_FILE_NAME, encryFileName);
        requestParamMap.put(WXQueryKey.FROM_USER, fromUser);
        requestParamMap.put(WXQueryKey.MEDIA_ID, mediaId);
        requestParamMap.put(WXQueryKey.PASS_TICKET, passTicket);
        requestParamMap.put(WXQueryKey.SENDER, sender);
        requestParamMap.put(WXQueryKey.WEB_WX_DATA_TICKET, webWxDataTicket);

        return this;
    }

    @Override
    public WebWxGetMediaReq setFileStreamAvailable(boolean fileStreamAvailable) {
        this.fileStreamAvailable = true;
        return this;
    }
}
