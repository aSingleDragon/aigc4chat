package pers.hll.aigc4chat.protocol.wechat.request;

import lombok.EqualsAndHashCode;
import pers.hll.aigc4chat.base.constant.ContentType;
import pers.hll.aigc4chat.protocol.wechat.constant.DefaultConfig;
import pers.hll.aigc4chat.protocol.wechat.constant.WXHeaderKey;
import pers.hll.aigc4chat.protocol.wechat.constant.WXHeaderValue;
import pers.hll.aigc4chat.protocol.wechat.constant.WXQueryKey;

import java.util.Map;

/**
 * 获取视频请求
 *
 * @author hll
 * @since 2024/03/11
 */
@EqualsAndHashCode(callSuper = true)
public class WebWxGetVideoReq extends BaseRequest<WebWxGetVideoReq, Object> {

    private long msgId;

    private String skey;

    private String passTicket;

    public WebWxGetVideoReq(String uri) {
        super(uri);
    }

    public WebWxGetVideoReq setMsgId(long msgId) {
        this.msgId = msgId;
        return this;
    }

    public WebWxGetVideoReq setSkey(String sKey) {
        this.skey = sKey;
        return this;
    }

    public WebWxGetVideoReq setPassTicket(String passTicket) {
        this.passTicket = passTicket;
        return this;
    }

    @Override
    public WebWxGetVideoReq build() {

        Map<String, String> headerMap = getHeaderMap();
        headerMap.put(WXHeaderKey.USER_AGENT, DefaultConfig.USER_AGENT);
        headerMap.put(WXHeaderKey.CONTENT_TYPE, ContentType.JSON);
        headerMap.put(WXHeaderKey.RANGE, WXHeaderValue.BYTES);

        Map<String, Object> requestParamMap = getRequestParamMap();
        requestParamMap.put(WXQueryKey.MSG_ID, String.valueOf(msgId));
        requestParamMap.put(WXQueryKey.SKEY, skey);
        requestParamMap.put(WXQueryKey.PASS_TICKET, passTicket);
        return this;
    }

    @Override
    public WebWxGetVideoReq setFileStreamAvailable(boolean fileStreamAvailable) {
        this.fileStreamAvailable = true;
        return this;
    }
}
