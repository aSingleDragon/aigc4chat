package pers.hll.aigc4chat.protocol.wechat.request;

import lombok.EqualsAndHashCode;
import pers.hll.aigc4chat.base.constant.ContentType;
import pers.hll.aigc4chat.protocol.wechat.constant.DefaultConfig;
import pers.hll.aigc4chat.protocol.wechat.constant.WXHeaderKey;
import pers.hll.aigc4chat.protocol.wechat.constant.WXQueryKey;

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

    private String skey;

    private String passTicket;

    public WebWxGetVoiceReq(String uri) {
        super(uri);
    }

    public WebWxGetVoiceReq setMsgId(long msgId) {
        this.msgId = msgId;
        return this;
    }

    public WebWxGetVoiceReq setSkey(String sKey) {
        this.skey = sKey;
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
        requestParamMap.put(WXQueryKey.SKEY, skey);
        requestParamMap.put(WXQueryKey.PASS_TICKET, passTicket);
        return this;
    }

    @Override
    public WebWxGetVoiceReq setFileStreamAvailable(boolean fileStreamAvailable) {
        this.fileStreamAvailable = true;
        return this;
    }
}
