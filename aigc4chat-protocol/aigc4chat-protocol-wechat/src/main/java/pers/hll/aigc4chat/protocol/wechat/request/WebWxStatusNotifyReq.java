package pers.hll.aigc4chat.protocol.wechat.request;

import lombok.EqualsAndHashCode;
import pers.hll.aigc4chat.base.constant.ContentType;
import pers.hll.aigc4chat.base.util.BaseUtil;
import pers.hll.aigc4chat.protocol.wechat.constant.DefaultConfig;
import pers.hll.aigc4chat.protocol.wechat.constant.WXHeaderKey;
import pers.hll.aigc4chat.protocol.wechat.constant.WXQueryKey;
import pers.hll.aigc4chat.protocol.wechat.constant.WXQueryValue;
import pers.hll.aigc4chat.protocol.wechat.request.body.BaseRequestBody;
import pers.hll.aigc4chat.protocol.wechat.request.body.WebWxStatusNotifyReqBody;
import pers.hll.aigc4chat.protocol.wechat.response.WebWxStatusNotifyResp;

import java.util.Map;

/**
 * 同步状态通知请求
 *
 * @author hll
 * @since 2024/03/11
 */
@EqualsAndHashCode(callSuper = true)
public class WebWxStatusNotifyReq extends BasePostRequest<WebWxStatusNotifyReq, WebWxStatusNotifyResp> {

    private String passTicket;

    private int notifyCode;

    private String userName;

    public WebWxStatusNotifyReq(String uri) {
        super(uri);
    }

    public WebWxStatusNotifyReq setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public WebWxStatusNotifyReq setNotifyCode(int notifyCode) {
        this.notifyCode = notifyCode;
        return this;
    }

    @Override
    public WebWxStatusNotifyReq build() {

        Map<String, String> headerMap = getHeaderMap();
        headerMap.put(WXHeaderKey.USER_AGENT, DefaultConfig.USER_AGENT);
        headerMap.put(WXHeaderKey.CONTENT_TYPE, ContentType.JSON);

        Map<String, Object> requestParamMap = getRequestParamMap();
        requestParamMap.put(WXQueryKey.LANG, WXQueryValue.ZH_CN);
        requestParamMap.put(WXQueryKey.PASS_TICKET, passTicket);

        return this;
    }

    @Override
    public String buildRequestBody() {
        return BaseUtil.GSON.toJson(new WebWxStatusNotifyReqBody()
                .setCode(notifyCode)
                .setFromUserName(userName)
                .setToUserName(userName)
                .setClientMsgId(BaseUtil.getEpochSecond())
                .setBaseRequestBody(this.getBaseRequestBody()));
    }

    @Override
    public WebWxStatusNotifyResp convertRespBodyToObj(String strEntity) {
        return BaseUtil.GSON.fromJson(strEntity, WebWxStatusNotifyResp.class);
    }

    public WebWxStatusNotifyReq setPassTicket(String passTicket) {
        this.passTicket = passTicket;
        return this;
    }

    @Override
    public WebWxStatusNotifyReq setBaseRequestBody(BaseRequestBody baseRequestBody) {
        this.baseRequestBody = baseRequestBody;
        return this;
    }
}
