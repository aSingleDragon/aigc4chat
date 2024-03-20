package pers.hll.aigc4chat.common.protocol.wechat.protocol.request;

import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import pers.hll.aigc4chat.common.base.util.BaseUtil;
import pers.hll.aigc4chat.common.base.util.XmlUtil;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.constant.DefaultConfig;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.constant.WXHeaderKey;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.constant.WXQueryKey;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.request.body.WebWxLogoutReqBody;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.request.body.WebWxOpLogReqBody;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.response.WxNewLoginPageResp;

import java.util.Map;

/**
 * @author hll
 * @since 2023/03/11
 */
@Slf4j
@EqualsAndHashCode(callSuper = true)
public class WebWxLogoutReq extends BasePostRequest<WebWxLogoutReq, Object> {

    private String sKey;

    private String sid;

    private String uin;

    public WebWxLogoutReq(String uri) {
        super(uri);
    }

    public WebWxLogoutReq setSKey(String sKey) {
        this.sKey = sKey;
        return this;
    }

    public WebWxLogoutReq setSid(String sId) {
        this.sid = sid;
        return this;
    }

    public WebWxLogoutReq setUin(String uin) {
        this.uin = uin;
        return this;
    }

    @Override
    public Object convertRespBodyToObj(String strEntity) {
        log.info("", strEntity);
        return null;
    }

    @Override
    public String buildRequestBody() {
        return BaseUtil.GSON.toJson(new WebWxLogoutReqBody(sid, uin));
    }

    @Override
    public WebWxLogoutReq build() {

        Map<String, Object> requestParamMap = this.getRequestParamMap();
        requestParamMap.put(WXQueryKey.REDIRECT, 1);
        requestParamMap.put(WXQueryKey.TYPE, 0);
        requestParamMap.put(WXQueryKey.SKEY, sKey);

        return this;
    }

    @Override
    public WebWxLogoutReq setRedirectsEnabled(boolean redirectsEnabled) {
        this.redirectsEnabled = redirectsEnabled;
        return this;
    }
}
