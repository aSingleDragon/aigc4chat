package pers.hll.aigc4chat.common.protocol.wechat.protocol.request;

import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import pers.hll.aigc4chat.common.base.util.BaseUtil;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.constant.DefaultConfig;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.constant.WXHeaderKey;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.constant.WXQueryKey;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.constant.WXQueryValue;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.request.body.BaseRequestBody;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.request.body.WebWxOpLogReqBody;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.response.BaseResponseBaseBody;

import java.util.Map;

/**
 * 修改个人资料请求
 *
 * @author hll
 * @since 2023/03/11
 */
@Slf4j
@EqualsAndHashCode(callSuper = true)
public class WebWxOpLogReq extends BasePostRequest<WebWxOpLogReq, BaseResponseBaseBody> {

    private String passTicket;

    private int cmdId;

    private int op;

    private String userName;

    private String remarkName;

    private String verifyTicket;

    private String verifyContent;

    public WebWxOpLogReq(String uri) {
        super(uri);
    }

    public WebWxOpLogReq setPassTicket(String passTicket) {
        this.passTicket = passTicket;
        return this;
    }

    public WebWxOpLogReq setOp(int op) {
        this.op = op;
        return this;
    }

    public WebWxOpLogReq setCmdId(int cmdId) {
        this.cmdId = cmdId;
        return this;
    }

    public WebWxOpLogReq setRemarkName(String remarkName) {
        this.remarkName = remarkName;
        return this;
    }

    public WebWxOpLogReq setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public WebWxOpLogReq setVerifyTicket(String verifyTicket) {
        this.verifyTicket = verifyTicket;
        return this;
    }

    public WebWxOpLogReq setVerifyContent(String verifyContent) {
        this.verifyContent = verifyContent;
        return this;
    }

    @Override
    public WebWxOpLogReq build() {

        Map<String, String> headerMap = getHeaderMap();
        headerMap.put(WXHeaderKey.USER_AGENT, DefaultConfig.USER_AGENT);

        Map<String, Object> requestParamMap = getRequestParamMap();
        requestParamMap.put(WXQueryKey.LANG, WXQueryValue.ZH_CN);
        requestParamMap.put(WXQueryKey.PASS_TICKET, passTicket);

        return this;
    }

    @Override
    public BaseResponseBaseBody convertRespBodyToObj(String strEntity) {
        return BaseUtil.GSON.fromJson(strEntity, BaseResponseBaseBody.class);
    }

    @Override
    public WebWxOpLogReq setBaseRequestBody(BaseRequestBody baseRequestBody) {
        this.baseRequestBody = baseRequestBody;
        return this;
    }

    @Override
    public String buildRequestBody() {
        return BaseUtil.GSON.toJson(new WebWxOpLogReqBody()
                .setCmdId(cmdId)
                .setOp(op)
                .setUserName(userName)
                .setRemarkName(remarkName)
                .setBaseRequestBody(this.getBaseRequestBody()));
    }
}
