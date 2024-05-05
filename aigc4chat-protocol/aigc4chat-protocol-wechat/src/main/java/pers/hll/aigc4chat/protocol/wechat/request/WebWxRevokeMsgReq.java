package pers.hll.aigc4chat.protocol.wechat.request;

import lombok.EqualsAndHashCode;
import pers.hll.aigc4chat.base.constant.ContentType;
import pers.hll.aigc4chat.base.util.BaseUtil;
import pers.hll.aigc4chat.protocol.wechat.constant.DefaultConfig;
import pers.hll.aigc4chat.protocol.wechat.constant.WXHeaderKey;
import pers.hll.aigc4chat.protocol.wechat.request.body.BaseRequestBody;
import pers.hll.aigc4chat.protocol.wechat.request.body.WebWxRevokeMsgReqBody;
import pers.hll.aigc4chat.protocol.wechat.response.WebWxRevokeMsgResp;

import java.util.Map;

/**
 * 撤回消息请求
 *
 * @author hll
 * @since 2024/03/11
 */
@EqualsAndHashCode(callSuper = true)
public class WebWxRevokeMsgReq extends BasePostRequest<WebWxRevokeMsgReq, WebWxRevokeMsgResp> {

    private String clientMsgId;

    private String svrMsgId;

    private String toUserName;

    public WebWxRevokeMsgReq(String uri) {
        super(uri);
    }

    @Override
    public WebWxRevokeMsgReq build() {

        Map<String, String> headerMap = getHeaderMap();
        headerMap.put(WXHeaderKey.USER_AGENT, DefaultConfig.USER_AGENT);
        headerMap.put(WXHeaderKey.CONTENT_TYPE, ContentType.JSON);
        return this;
    }

    @Override
    public String buildRequestBody() {
        return BaseUtil.GSON.toJson(new WebWxRevokeMsgReqBody()
                        .setClientMsgId(clientMsgId)
                        .setSvrMsgId(svrMsgId)
                        .setToUserName(toUserName)
                        .setBaseRequestBody(this.getBaseRequestBody()));
    }

    @Override
    public WebWxRevokeMsgResp convertRespBodyToObj(String strEntity) {
        return BaseUtil.GSON.fromJson(strEntity, WebWxRevokeMsgResp.class);
    }

    public WebWxRevokeMsgReq setClientMsgId(String clientMsgId) {
        this.clientMsgId = clientMsgId;
        return this;
    }

    public WebWxRevokeMsgReq setSvrMsgId(String svrMsgId) {
        this.svrMsgId = svrMsgId;
        return this;
    }

    public WebWxRevokeMsgReq setToUserName(String toUserName) {
        this.toUserName = toUserName;
        return this;
    }

    @Override
    public WebWxRevokeMsgReq setBaseRequestBody(BaseRequestBody baseRequestBody) {
        this.baseRequestBody = baseRequestBody;
        return this;
    }
}
