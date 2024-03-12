package pers.hll.aigc4chat.common.protocol.wechat.protocol.request;

import lombok.Builder;
import org.dom4j.DocumentException;
import pers.hll.aigc4chat.common.base.util.XmlUtil;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.constant.DefaultConfig;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.constant.WXEndPoint;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.constant.WXHeaderKey;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.response.WxNewLoginPageResp;

import java.util.Map;

/**
 *
 * @author hll
 * @since 2023/03/11
 */
public class WxNewLoginPageReq extends BaseRequest {

    public WxNewLoginPageReq(String uri) {
        super(uri);
        this.setRedirectsEnabled(false);
        Map<String, String> headerMap = this.getHeaderMap();
        headerMap.put(WXHeaderKey.USER_AGENT, DefaultConfig.USER_AGENT);
        headerMap.put(WXHeaderKey.CLIENT_VERSION, DefaultConfig.UOS_PATCH_CLIENT_VERSION);
        headerMap.put(WXHeaderKey.EXT_SPAM, DefaultConfig.UOS_PATCH_EXTSPAM);
        headerMap.put(WXHeaderKey.REFERER, DefaultConfig.REFERER);
    }

    @Override
    public WxNewLoginPageResp stringToGeneric(String strEntity) {
        return XmlUtil.xmlStrToObject(strEntity, WxNewLoginPageResp.class);
    }

    public static void main(String[] args) throws DocumentException {
        String strEntity = "<error>\n" +
                "    <ret>0</ret>\n" +
                "    <message></message>\n" +
                "    <skey>@crypt_8b7318fd_d8819ef6043a3e5eaf24b89e09f0bdc0</skey>\n" +
                "    <wxsid>PNWg3LQayQU8fCCG</wxsid>\n" +
                "    <wxuin>2977348135</wxuin>\n" +
                "    <pass_ticket>XpIfrzRRUnnmFUWq0I%2FTfRwEHWIFRSPduSbTTyD%2Fqf0e3N052affhbgqdi64OA0BcKpCOTR9UQKAjEHhDL28hw%3D%3D</pass_ticket>\n" +
                "    <isgrayscale>1</isgrayscale>\n" +
                "   </error>";
        WxNewLoginPageResp wxNewLoginPageResp = XmlUtil.xmlStrToObject(strEntity, WxNewLoginPageResp.class);
        System.out.println(wxNewLoginPageResp.toString());
    }
}
