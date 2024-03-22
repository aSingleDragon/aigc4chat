package pers.hll.aigc4chat.common.protocol.wechat.protocol.request;

import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import pers.hll.aigc4chat.common.base.constant.ContentType;
import pers.hll.aigc4chat.common.base.util.BaseUtil;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.constant.DefaultConfig;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.constant.WXHeaderKey;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.constant.WXQueryKey;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.request.body.BaseRequestBody;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.request.body.VerifyUser;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.request.body.WebWxVerifyUserReqBody;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.response.BaseResponseBaseBody;

import java.util.List;
import java.util.Map;

/**
 * 验证好友请求
 *
 * @author hll
 * @since 2023/03/11
 */
@Slf4j
@EqualsAndHashCode(callSuper = true)
public class WebWxVerifyUserReq extends BasePostRequest<WebWxVerifyUserReq, BaseResponseBaseBody> {

    private String passTicket;

    private int opCode;

    private String userName;

    private String verifyTicket;

    private String verifyContent;

    public WebWxVerifyUserReq(String uri) {
        super(uri);
    }

    public WebWxVerifyUserReq setPassTicket(String passTicket) {
        this.passTicket = passTicket;
        return this;
    }

    public WebWxVerifyUserReq setOpCode(int opCode) {
        this.opCode = opCode;
        return this;
    }

    public WebWxVerifyUserReq setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public WebWxVerifyUserReq setVerifyTicket(String verifyTicket) {
        this.verifyTicket = verifyTicket;
        return this;
    }

    public WebWxVerifyUserReq setVerifyContent(String verifyContent) {
        this.verifyContent = verifyContent;
        return this;
    }

    @Override
    public WebWxVerifyUserReq build() {

        Map<String, String> headerMap = getHeaderMap();
        headerMap.put(WXHeaderKey.CONTENT_TYPE, ContentType.JSON);
        headerMap.put(WXHeaderKey.USER_AGENT, DefaultConfig.USER_AGENT);

        Map<String, Object> requestParamMap = getRequestParamMap();
        requestParamMap.put(WXQueryKey.R, BaseUtil.getEpochSecond());
        requestParamMap.put(WXQueryKey.PASS_TICKET, passTicket);

        return this;
    }

    @Override
    public BaseResponseBaseBody convertRespBodyToObj(String strEntity) {
        return BaseUtil.GSON.fromJson(strEntity, BaseResponseBaseBody.class);
    }

    @Override
    public WebWxVerifyUserReq setBaseRequestBody(BaseRequestBody baseRequestBody) {
        this.baseRequestBody = baseRequestBody;
        return this;
    }

    @Override
    public String buildRequestBody() {
        return BaseUtil.GSON.toJson(new WebWxVerifyUserReqBody()
                .setOpCode(opCode)
                .setVerifyUserListSize(1)
                .setVerifyUserList(List.of(new VerifyUser(userName, verifyTicket)))
                .setVerifyContent(verifyContent)
                .setSceneListCount(1)
                .setSceneList(List.of(33))
                .setSkey(this.getBaseRequestBody().getSKey())
                .setBaseRequestBody(this.getBaseRequestBody()));
    }
}
