package pers.hll.aigc4chat.common.protocol.wechat.protocol.request;

import lombok.EqualsAndHashCode;
import pers.hll.aigc4chat.common.base.constant.ContentType;
import pers.hll.aigc4chat.common.base.util.BaseUtil;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.constant.DefaultConfig;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.constant.WXHeaderKey;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.constant.WXQueryKey;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.constant.WXQueryValue;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.request.body.BaseRequestBody;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.request.body.Contact;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.request.body.WebWxBatchGetContactReqBody;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.response.WebWxBatchGetContactResp;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.response.WebWxGetContactResp;

import java.util.List;
import java.util.Map;

/**
 * @author hll
 * @since 2023/03/11
 */
@EqualsAndHashCode(callSuper = true)
public class WebWxGetContactReq extends BaseRequest<WebWxGetContactReq, WebWxGetContactResp> {

    private int seq;

    private String sKey;

    private String passTicket;

    public WebWxGetContactReq(String uri) {
        super(uri);
    }

    public WebWxGetContactReq setSeq(int seq) {
        this.seq = seq;
        return this;
    }

    public WebWxGetContactReq setSKey(String sKey) {
        this.sKey = sKey;
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
        requestParamMap.put(WXQueryKey.SKEY, sKey);
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
