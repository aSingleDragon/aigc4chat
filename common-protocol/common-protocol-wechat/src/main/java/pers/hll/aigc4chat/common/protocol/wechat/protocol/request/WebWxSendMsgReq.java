package pers.hll.aigc4chat.common.protocol.wechat.protocol.request;

import lombok.EqualsAndHashCode;
import pers.hll.aigc4chat.common.base.constant.ContentType;
import pers.hll.aigc4chat.common.base.util.BaseUtil;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.constant.DefaultConfig;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.constant.WXHeaderKey;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.constant.WXQueryKey;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.request.body.BaseRequestBody;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.request.body.Msg;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.request.body.WebWxSendMsgReqBody;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.response.WebWxSendMsgResp;

import java.util.Map;

/**
 *
 * @author hll
 * @since 2023/03/11
 */
@EqualsAndHashCode(callSuper = true)
public class WebWxSendMsgReq extends BasePostRequest<WebWxSendMsgReq, WebWxSendMsgResp> {

    private Msg msg;

    public WebWxSendMsgReq(String uri) {
        super(uri);
    }

    @Override
    public WebWxSendMsgReq build() {
        Map<String, String> headerMap = getHeaderMap();
        headerMap.put(WXHeaderKey.USER_AGENT, DefaultConfig.USER_AGENT);
        headerMap.put(WXHeaderKey.CONTENT_TYPE, ContentType.JSON);
        return this;
    }

    @Override
    public String buildRequestBody() {
        return BaseUtil.GSON.toJson(new WebWxSendMsgReqBody()
                        .setMsg(msg)
                        .setBaseRequestBody(this.getBaseRequestBody()));
    }

    @Override
    public WebWxSendMsgResp convertRespBodyToObj(String strEntity) {
        return BaseUtil.GSON.fromJson(strEntity, WebWxSendMsgResp.class);
    }

    public WebWxSendMsgReq setMsg(Msg msg) {
        this.msg = msg;
        return this;
    }

    @Override
    public WebWxSendMsgReq setBaseRequestBody(BaseRequestBody baseRequestBody) {
        this.baseRequestBody = baseRequestBody;
        return this;
    }
}
