package pers.hll.aigc4chat.common.protocol.wechat.protocol.request;

import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import pers.hll.aigc4chat.common.base.constant.ContentType;
import pers.hll.aigc4chat.common.base.util.BaseUtil;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.constant.DefaultConfig;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.constant.WXHeaderKey;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.constant.WXQueryKey;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.request.body.BaseRequestBody;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.request.body.WebWxSyncReqBody;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.response.WebWxSyncResp;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.response.webwxinit.SyncKey;

import java.util.Map;

/**
 * 同步数据请求
 *
 * @author hll
 * @since 2023/03/11
 */
@Slf4j
@EqualsAndHashCode(callSuper = true)
public class WebWxSyncReq extends BasePostRequest<WebWxSyncReq, WebWxSyncResp> {

    private String sId;

    private String sKey;

    private String passTicket;

    private SyncKey syncKey;

    public WebWxSyncReq(String uri) {
        super(uri);
    }

    public WebWxSyncReq setSId(String sId) {
        this.sId = sId;
        return this;
    }

    public WebWxSyncReq setSKey(String sKey) {
        this.sKey = sKey;
        return this;
    }

    public WebWxSyncReq setPassTicket(String passTicket) {
        this.passTicket = passTicket;
        return this;
    }

    public WebWxSyncReq setSyncKey(SyncKey syncKey) {
        this.syncKey = syncKey;
        return this;
    }

    @Override
    public WebWxSyncReq build() {

        Map<String, String> headerMap = getHeaderMap();
        headerMap.put(WXHeaderKey.CONTENT_TYPE, ContentType.JSON);
        headerMap.put(WXHeaderKey.USER_AGENT, DefaultConfig.USER_AGENT);

        Map<String, Object> requestParamMap = getRequestParamMap();
        requestParamMap.put(WXQueryKey.SID, sId);
        requestParamMap.put(WXQueryKey.SKEY, sKey);
        requestParamMap.put(WXQueryKey.PASS_TICKET, passTicket);

        return this;
    }

    @Override
    public WebWxSyncResp convertRespBodyToObj(String strEntity) {
        return BaseUtil.GSON.fromJson(strEntity, WebWxSyncResp.class);
    }

    @Override
    public WebWxSyncReq setBaseRequestBody(BaseRequestBody baseRequestBody) {
        this.baseRequestBody = baseRequestBody;
        return this;
    }

    @Override
    public String buildRequestBody() {
        return BaseUtil.GSON.toJson(new WebWxSyncReqBody()
                .setRr(~BaseUtil.getEpochSecond())
                .setSyncKey(syncKey)
                .setBaseRequestBody(this.getBaseRequestBody()));
    }
}
