package pers.hll.aigc4chat.common.protocol.wechat.protocol.request;

import pers.hll.aigc4chat.common.base.util.XmlUtil;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.constant.DefaultConfig;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.constant.WXHeaderKey;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.response.WxNewLoginPageResp;

import java.util.Map;

/**
 *
 * @author hll
 * @since 2023/03/11
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
        headerMap.put(WXHeaderKey.EXT_SPAM, DefaultConfig.UOS_PATCH_EXTSPAM);
        headerMap.put(WXHeaderKey.REFERER, DefaultConfig.REFERER);

        return this;
    }
    @Override
    public WxNewLoginPageReq setRedirectsEnabled(boolean redirectsEnabled) {
        this.redirectsEnabled = redirectsEnabled;
        return this;
    }
}
