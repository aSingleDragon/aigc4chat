package pers.hll.aigc4chat.common.protocol.wechat.protocol.request;

import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import pers.hll.aigc4chat.common.base.constant.StringPool;
import pers.hll.aigc4chat.common.base.util.BaseUtil;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.constant.WXQueryKey;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.response.LoginResp;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author hll
 * @since 2023/03/13
 */
@Slf4j
@EqualsAndHashCode(callSuper = true)
public class LoginReq extends BaseRequest<LoginReq, LoginResp> {

    private String uuid;

    private int firstLogin;

    public LoginReq(String uri) {
        super(uri);
    }

    public LoginReq setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public LoginReq setFirstLogin(int firstLogin) {
        this.firstLogin = firstLogin;
        return this;
    }

    @Override
    public LoginResp convertRespBodyToObj(String strEntity) {
        Matcher matcher = Pattern.compile(
                "window.code=(\\d{3});(window.userAvatar = '(.+)'|\\swindow.redirect_uri=\"(.+)\")?")
                .matcher(strEntity);
        if (matcher.find()) {
            return LoginResp.builder()
                    .code(Integer.parseInt(matcher.group(1)))
                    .userAvatar(matcher.group(3))
                    .redirectUri(matcher.group(4))
                    .build();
        }
        log.error("解析登录请求响应数据失败: {}", strEntity);
        return null;
    }

    @Override
    public LoginReq build() {
        long epochSecond = BaseUtil.getEpochSecond();
        Map<String, Object> requestParamMap = getRequestParamMap();
        requestParamMap.put(WXQueryKey.LOGIN_ICON, String.valueOf(true));
        requestParamMap.put(WXQueryKey.UUID, uuid);
        requestParamMap.put(WXQueryKey.TIP, firstLogin);
        requestParamMap.put(WXQueryKey.R, (int) ((-epochSecond) / 1579));
        requestParamMap.put(StringPool.UNDERSCORE, epochSecond);
        return this;
    }
}
