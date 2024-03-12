package pers.hll.aigc4chat.common.protocol.wechat.protocol.response;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author hll
 * @author 2024/03/10
 */
public class RspLogin {

    public int code;

    public String userAvatar;

    public String redirectUri;

    public RspLogin(String str) {
        Matcher matcher = Pattern.compile(
                "window.code=(\\d{3});(window.userAvatar = '(.+)'|\\swindow.redirect_uri=\"(.+)\")?").matcher(str);
        if (matcher.find()) {
            code = Integer.parseInt(matcher.group(1));
            userAvatar = matcher.group(3);
            redirectUri = matcher.group(4);
        }
    }
}
