package pers.hll.aigc4chat.protocol.wechat.request;

import lombok.EqualsAndHashCode;
import pers.hll.aigc4chat.base.constant.ContentType;
import pers.hll.aigc4chat.base.util.BaseUtil;
import pers.hll.aigc4chat.protocol.wechat.constant.DefaultConfig;
import pers.hll.aigc4chat.protocol.wechat.constant.WXHeaderKey;
import pers.hll.aigc4chat.protocol.wechat.constant.WXQueryKey;
import pers.hll.aigc4chat.protocol.wechat.response.WebWxGetContactResp;

import java.util.Map;

/**
 * 获取联系人请求
 *
 * @author hll
 * @since 2024/03/11
 */
@EqualsAndHashCode(callSuper = true)
public class WebWxGetContactReq extends BaseRequest<WebWxGetContactReq, WebWxGetContactResp> {

    private int seq;

    private String skey;

    private String passTicket;

    public WebWxGetContactReq(String uri) {
        super(uri);
    }

    public WebWxGetContactReq setSeq(int seq) {
        this.seq = seq;
        return this;
    }

    public WebWxGetContactReq setSkey(String sKey) {
        this.skey = sKey;
        return this;
    }

    public WebWxGetContactReq setPassTicket(String passTicket) {
        this.passTicket = passTicket;
        return this;
    }

    @Override
    public WebWxGetContactReq build() {

        Map<String, Object> requestParamMap = getRequestParamMap();
        requestParamMap.put(WXQueryKey.R, BaseUtil.getEpochSecond());
        requestParamMap.put(WXQueryKey.SKEY, skey);
        requestParamMap.put(WXQueryKey.SEQ, seq);

        Map<String, String> headerMap = getHeaderMap();
        headerMap.put(WXHeaderKey.CONTENT_TYPE, ContentType.JSON);
        headerMap.put(WXHeaderKey.USER_AGENT, DefaultConfig.USER_AGENT);
        return this;
    }

    @Override
    public WebWxGetContactResp convertRespBodyToObj(String strEntity) {
        return BaseUtil.GSON.fromJson(strEntity, WebWxGetContactResp.class);
    }
}
