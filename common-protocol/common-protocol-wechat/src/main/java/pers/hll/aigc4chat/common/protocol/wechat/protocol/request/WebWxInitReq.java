package pers.hll.aigc4chat.common.protocol.wechat.protocol.request;

import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import pers.hll.aigc4chat.common.base.constant.ContentType;
import pers.hll.aigc4chat.common.base.util.BaseUtil;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.constant.DefaultConfig;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.constant.WXHeaderKey;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.constant.WXQueryKey;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.request.body.BaseRequestBody;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.request.body.WebWxInitReqBody;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.response.WebWxInitResp;

import java.util.Map;

/**
 *
 * @author hll
 * @since 2023/03/11
 */
@Slf4j
@EqualsAndHashCode(callSuper = true)
public class WebWxInitReq extends BasePostRequest<WebWxInitReq, WebWxInitResp> {

    private String passTicket;

    public WebWxInitReq(String uri) {
        super(uri);
    }

    @Override
    public WebWxInitReq build() {

        Map<String, Object> requestParamMap = getRequestParamMap();
        requestParamMap.put(WXQueryKey.R, BaseUtil.getR());
        requestParamMap.put(WXQueryKey.PASS_TICKET, passTicket);

        Map<String, String> headerMap = getHeaderMap();
        headerMap.put(WXHeaderKey.CONTENT_TYPE, ContentType.JSON);
        headerMap.put(WXHeaderKey.USER_AGENT, DefaultConfig.USER_AGENT);

        return this;
    }

    @Override
    public String buildRequestBody() {
        return BaseUtil.GSON.toJson(new WebWxInitReqBody().setBaseRequestBody(this.getBaseRequestBody()));
    }

    @Override
    public WebWxInitResp convertRespBodyToObj(String strEntity) {
        return BaseUtil.GSON.fromJson(strEntity, WebWxInitResp.class);
    }

    public WebWxInitReq setPassTicket(String passTicket) {
        this.passTicket = passTicket;
        return this;
    }

    @Override
    public WebWxInitReq setBaseRequestBody(BaseRequestBody baseRequestBody) {
        this.baseRequestBody = baseRequestBody;
        return this;
    }
}
