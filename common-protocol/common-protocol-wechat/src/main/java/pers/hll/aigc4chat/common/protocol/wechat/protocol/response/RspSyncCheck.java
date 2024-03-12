package pers.hll.aigc4chat.common.protocol.wechat.protocol.response;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author hll
 * @author 2024/03/10
 */
public class RspSyncCheck {

    public int retcode;

    public int selector;

    public RspSyncCheck(String str) {
        Matcher matcher = Pattern.compile("window.synccheck=\\{retcode:\"(\\d*)\",selector:\"(\\d*)\"\\}").matcher(str);
        if (matcher.find()) {
            retcode = Integer.parseInt(matcher.group(1));
            selector = Integer.parseInt(matcher.group(2));
        }
    }
}
