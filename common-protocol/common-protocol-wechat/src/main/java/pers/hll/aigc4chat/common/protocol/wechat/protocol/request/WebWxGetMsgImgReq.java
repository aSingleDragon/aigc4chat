package pers.hll.aigc4chat.common.protocol.wechat.protocol.request;

import lombok.EqualsAndHashCode;
import pers.hll.aigc4chat.common.base.constant.ContentType;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.constant.DefaultConfig;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.constant.WXHeaderKey;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.constant.WXQueryKey;

import java.util.Map;

/**
 * @author hll
 * @since 2023/03/11
 */
@EqualsAndHashCode(callSuper = true)
public class WebWxGetMsgImgReq extends BaseRequest<WebWxGetMsgImgReq, Object> {

    private long msgId;

    private String sKey;

    private String type;

    private String passTicket;

    public WebWxGetMsgImgReq(String uri) {
        super(uri);
    }

    public WebWxGetMsgImgReq setMsgId(long msgId) {
        this.msgId = msgId;
        return this;
    }

    public WebWxGetMsgImgReq setSKey(String sKey) {
        this.sKey = sKey;
        return this;
    }

    public WebWxGetMsgImgReq setType(String type) {
        this.type = type;
        return this;
    }

    public WebWxGetMsgImgReq setPassTicket(String passTicket) {
        this.passTicket = passTicket;
        return this;
    }

    @Override
    public WebWxGetMsgImgReq build() {

        Map<String, String> headerMap = getHeaderMap();
        headerMap.put(WXHeaderKey.USER_AGENT, DefaultConfig.USER_AGENT);
        headerMap.put(WXHeaderKey.CONTENT_TYPE, ContentType.JSON);

        Map<String, Object> requestParamMap = getRequestParamMap();
        requestParamMap.put(WXQueryKey.MSG_ID, String.valueOf(msgId));
        requestParamMap.put(WXQueryKey.SKEY, sKey);
        requestParamMap.put(WXQueryKey.TYPE, type);
        requestParamMap.put(WXQueryKey.PASS_TICKET, passTicket);
        return this;
    }

    @Override
    public WebWxGetMsgImgReq setFileStreamAvailable(boolean fileStreamAvailable) {
        this.fileStreamAvailable = true;
        return this;
    }
}
