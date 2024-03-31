package pers.hll.aigc4chat.common.protocol.wechat.protocol.request;

import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import pers.hll.aigc4chat.common.base.util.BaseUtil;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.constant.WXQueryKey;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.request.body.WebWxLogoutReqBody;

import java.util.Map;

/**
 * 登出请求
 *
 * @author hll
 * @since 2024/03/11
 */
@Slf4j
@EqualsAndHashCode(callSuper = true)
public class WebWxLogoutReq extends BaseRequest<WebWxLogoutReq, Object> {

    private String skey;

    public WebWxLogoutReq(String uri) {
        super(uri);
    }

    public WebWxLogoutReq setSkey(String sKey) {
        this.skey = sKey;
        return this;
    }

    @Override
    public Object convertRespBodyToObj(String strEntity) {
        log.info("登出响应: {}", strEntity);
        return null;
    }

    @Override
    public WebWxLogoutReq build() {

        Map<String, Object> requestParamMap = this.getRequestParamMap();
        requestParamMap.put(WXQueryKey.REDIRECT, 1);
        requestParamMap.put(WXQueryKey.TYPE, 1);
        requestParamMap.put(WXQueryKey.SKEY, skey);

        return this;
    }

    @Override
    public WebWxLogoutReq setRedirectsEnabled(boolean redirectsEnabled) {
        this.redirectsEnabled = redirectsEnabled;
        return this;
    }
}
