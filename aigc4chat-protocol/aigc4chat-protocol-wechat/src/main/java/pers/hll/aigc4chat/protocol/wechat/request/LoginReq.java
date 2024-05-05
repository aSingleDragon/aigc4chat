package pers.hll.aigc4chat.protocol.wechat.request;

import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import pers.hll.aigc4chat.base.constant.StringPool;
import pers.hll.aigc4chat.base.exception.BizException;
import pers.hll.aigc4chat.base.util.BaseUtil;
import pers.hll.aigc4chat.protocol.wechat.constant.WXQueryKey;
import pers.hll.aigc4chat.protocol.wechat.response.LoginResp;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 登录请求
 *
 * @author hll
 * @since 2024/03/13
 */
@Slf4j
@EqualsAndHashCode(callSuper = true)
public class LoginReq extends BaseRequest<LoginReq, LoginResp> {

    private String uuid;

    /**
     * 是否是第一次登录
     */
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
        throw BizException.of("解析登录请求响应数据失败: {}", strEntity);
    }

    @Override
    public LoginReq build() {
        long epochSecond = BaseUtil.getEpochSecond();
        Map<String, Object> requestParamMap = getRequestParamMap();
        requestParamMap.put(WXQueryKey.LOGIN_ICON, String.valueOf(true));
        requestParamMap.put(WXQueryKey.UUID, uuid);
        requestParamMap.put(WXQueryKey.TIP, 1);
        requestParamMap.put(WXQueryKey.R, (int) ((-epochSecond) / 1579));
        requestParamMap.put(StringPool.UNDERSCORE, epochSecond);
        return this;
    }
}
