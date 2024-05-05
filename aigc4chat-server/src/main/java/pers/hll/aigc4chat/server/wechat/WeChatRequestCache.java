package pers.hll.aigc4chat.server.wechat;

import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;
import pers.hll.aigc4chat.protocol.wechat.request.body.BaseRequestBody;
import pers.hll.aigc4chat.protocol.wechat.response.webwxinit.SyncKey;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 微信请求的一些数据的缓存
 *
 * @author hll
 * @since 2024/03/31
 */
public class WeChatRequestCache {

    private static final AtomicReference<NeededInfo> NEEDED_INFO = new AtomicReference<>();

    private WeChatRequestCache() {
    }

    public static NeededInfo getNeededInfo() {
        return NEEDED_INFO.get() == null ? setNeededInfo(new NeededInfo()) : NEEDED_INFO.get();
    }

    public static NeededInfo setNeededInfo(NeededInfo neededInfo) {
        NEEDED_INFO.set(neededInfo);
        return neededInfo;
    }

    public static void clearAuthInfo() {
        NEEDED_INFO.set(null);
    }

    public static String getHost() {
        return getNeededInfo().getHost();
    }

    public static String getPassTicket() {
        return getNeededInfo().getPassTicket();
    }

    public static BaseRequestBody getBaseRequestBody() {
        NeededInfo neededInfo = getNeededInfo();
        if (StringUtils.isNoneEmpty(neededInfo.getUin(), neededInfo.getSid(), neededInfo.getSkey())) {
            return new BaseRequestBody(neededInfo.getUin(), neededInfo.getSid(), neededInfo.getSkey());
        }
        throw new IllegalArgumentException("未获取到微信网页API需要的一些信息");
    }

    /**
     * 调用微信网页API需要的一些信息
     *
     * @author hll
     * @since 2024/03/31
     */
    @Data
    @Accessors(chain = true)
    public static class NeededInfo {

        private String host;

        private String uin;

        private String sid;

        private String webWxDataTicket;

        private long time = 0;

        private int fileId = 0;

        private String skey;

        private String uuid;

        private String passTicket;

        private SyncKey syncKey;

        private SyncKey syncCheckKey;

        private String deviceId;
    }
}