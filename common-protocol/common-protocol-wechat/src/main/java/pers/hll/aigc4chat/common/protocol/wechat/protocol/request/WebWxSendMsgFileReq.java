package pers.hll.aigc4chat.common.protocol.wechat.protocol.request;

import lombok.EqualsAndHashCode;
import pers.hll.aigc4chat.common.base.constant.ContentType;
import pers.hll.aigc4chat.common.base.util.BaseUtil;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.constant.DefaultConfig;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.constant.WXHeaderKey;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.constant.WXQueryKey;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.constant.WXQueryValue;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.request.body.BaseRequestBody;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.request.body.Msg;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.request.body.WebWxSendMsgReqBody;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.response.WebWxSendMsgResp;

import java.util.Map;

/**
 * 发送文件请求
 *
 * @author hll
 * @since 2024/03/11
 */
@EqualsAndHashCode(callSuper = true)
public class WebWxSendMsgFileReq extends BasePostRequest<WebWxSendMsgFileReq, WebWxSendMsgResp> {

    private String passTicket;

    private Msg msg;

    private String fun;

    public WebWxSendMsgFileReq(String uri) {
        super(uri);
    }

    @Override
    public WebWxSendMsgFileReq build() {

        Map<String, Object> requestParamMap = getRequestParamMap();
        requestParamMap.put(WXQueryKey.FUN, fun);
        requestParamMap.put(WXQueryKey.F, WXQueryValue.JSON);

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

    public WebWxSendMsgFileReq setPassTicket(String passTicket) {
        this.passTicket = passTicket;
        return this;
    }

    public WebWxSendMsgFileReq setMsg(Msg msg) {
        this.msg = msg;
        return this;
    }

    public WebWxSendMsgFileReq setFun(String fun) {
        this.fun = fun;
        return this;
    }

    @Override
    public WebWxSendMsgFileReq setBaseRequestBody(BaseRequestBody baseRequestBody) {
        this.baseRequestBody = baseRequestBody;
        return this;
    }
}
