package pers.hll.aigc4chat.common.protocol.wechat.protocol.request;

import lombok.EqualsAndHashCode;
import pers.hll.aigc4chat.common.base.constant.ContentType;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.constant.DefaultConfig;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.constant.WXHeaderKey;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.constant.WXQueryKey;

import java.util.Map;

/**
 * 获取声音请求
 *
 * @author hll
 * @since 2024/03/11
 */
@EqualsAndHashCode(callSuper = true)
public class WebWxGetVoiceReq extends BaseRequest<WebWxGetVoiceReq, Object> {

    private long msgId;

    private String sKey;

    private String passTicket;

    public WebWxGetVoiceReq(String uri) {
        super(uri);
    }

    public WebWxGetVoiceReq setMsgId(long msgId) {
        this.msgId = msgId;
        return this;
    }

    public WebWxGetVoiceReq setSKey(String sKey) {
        this.sKey = sKey;
        return this;
    }

    public WebWxGetVoiceReq setPassTicket(String passTicket) {
        this.passTicket = passTicket;
        return this;
    }

    @Override
    public WebWxGetVoiceReq build() {

        Map<String, String> headerMap = getHeaderMap();
        headerMap.put(WXHeaderKey.USER_AGENT, DefaultConfig.USER_AGENT);
        headerMap.put(WXHeaderKey.CONTENT_TYPE, ContentType.JSON);

        Map<String, Object> requestParamMap = getRequestParamMap();
        requestParamMap.put(WXQueryKey.MSG_ID, String.valueOf(msgId));
        requestParamMap.put(WXQueryKey.SKEY, sKey);
        requestParamMap.put(WXQueryKey.PASS_TICKET, passTicket);
        return this;
    }

    @Override
    public WebWxGetVoiceReq setFileStreamAvailable(boolean fileStreamAvailable) {
        this.fileStreamAvailable = true;
        return this;
    }
}
