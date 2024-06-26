package pers.hll.aigc4chat.protocol.wechat.request;

import lombok.extern.slf4j.Slf4j;
import pers.hll.aigc4chat.base.constant.StringPool;
import pers.hll.aigc4chat.base.exception.BizException;
import pers.hll.aigc4chat.protocol.wechat.response.JsLoginResp;
import pers.hll.aigc4chat.protocol.wechat.constant.WXEndPoint;
import pers.hll.aigc4chat.protocol.wechat.constant.WXQueryKey;
import pers.hll.aigc4chat.protocol.wechat.constant.WXQueryValue;

import java.util.Map;

/**
 * js登录请求
 *
 * @author hll
 * @since 2024/03/11
 */
@Slf4j
public class JsLoginReq extends BaseRequest<JsLoginReq, JsLoginResp> {

    public JsLoginReq(String uri) {
        super(uri);
    }

    @Override
    public JsLoginResp convertRespBodyToObj(String strEntity) {
        // strEntity: [ window.QRLogin.code = 200; window.QRLogin.uuid = "YdhFiz5kzw=="; ]
        int code = 0;
        String uuid = null;
        try {
            String[] entities = strEntity.split(StringPool.SEMICOLON);
            code = Integer.parseInt(entities[0].split(StringPool.EQUALS)[1].trim());
            uuid = entities[1].split(StringPool.QUOTE)[1].trim();
        } catch (Exception e) {
            log.error("JsLoginReq 响应:[{}]解析失败, 异常信息:", strEntity, e);
            throw BizException.of("JsLoginReq 响应:[{}]解析失败, 异常信息:", strEntity, e);
        }
        return JsLoginResp.builder()
                .code(code)
                .uuid(uuid)
                .build();
    }

    @Override
    public JsLoginReq build() {

        Map<String, Object> requestParamMap = getRequestParamMap();
        requestParamMap.put(WXQueryKey.APP_ID, WXQueryValue.APPID);
        requestParamMap.put(WXQueryKey.FUN, WXQueryValue.NEW);
        requestParamMap.put(WXQueryKey.REDIRECT_URI, WXEndPoint.WEB_WX_NEW_LOGIN_PAGE);
        requestParamMap.put(WXQueryKey.LANG, WXQueryValue.ZH_CN);

        return this;
    }
}
