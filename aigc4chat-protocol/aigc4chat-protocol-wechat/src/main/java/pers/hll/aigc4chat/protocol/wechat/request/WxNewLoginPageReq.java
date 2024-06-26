package pers.hll.aigc4chat.protocol.wechat.request;

import pers.hll.aigc4chat.base.util.XmlUtil;
import pers.hll.aigc4chat.protocol.wechat.constant.DefaultConfig;
import pers.hll.aigc4chat.protocol.wechat.constant.WXHeaderKey;
import pers.hll.aigc4chat.protocol.wechat.response.WxNewLoginPageResp;

import java.util.Map;

/**
 * 跳转新登录页面请求
 *
 * @author hll
 * @since 2024/03/11
 */
public class WxNewLoginPageReq extends BaseRequest<WxNewLoginPageReq, WxNewLoginPageResp> {

    public WxNewLoginPageReq(String uri) {
        super(uri);
    }

    @Override
    public WxNewLoginPageResp convertRespBodyToObj(String strEntity) {
        return XmlUtil.xmlStrToObject(strEntity, WxNewLoginPageResp.class);
    }

    @Override
    public WxNewLoginPageReq build() {

        Map<String, String> headerMap = this.getHeaderMap();
        headerMap.put(WXHeaderKey.USER_AGENT, DefaultConfig.USER_AGENT);
        headerMap.put(WXHeaderKey.CLIENT_VERSION, DefaultConfig.UOS_PATCH_CLIENT_VERSION);
        headerMap.put(WXHeaderKey.EXT_SPAM, DefaultConfig.UOS_PATCH_EXT_SPAM);
        headerMap.put(WXHeaderKey.REFERER, DefaultConfig.REFERER);

        return this;
    }
    @Override
    public WxNewLoginPageReq setRedirectsEnabled(boolean redirectsEnabled) {
        this.redirectsEnabled = redirectsEnabled;
        return this;
    }
}
