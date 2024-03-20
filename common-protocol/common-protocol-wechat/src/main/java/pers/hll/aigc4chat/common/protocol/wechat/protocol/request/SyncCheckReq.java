package pers.hll.aigc4chat.common.protocol.wechat.protocol.request;

import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import pers.hll.aigc4chat.common.base.constant.ContentType;
import pers.hll.aigc4chat.common.base.constant.StringPool;
import pers.hll.aigc4chat.common.base.util.BaseUtil;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.constant.DefaultConfig;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.constant.WXHeaderKey;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.constant.WXQueryKey;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.response.SyncCheckResp;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.response.WebWxGetContactResp;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.response.webwxinit.SyncKey;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 *
 * @author hll
 * @since 2023/03/11
 */
@Slf4j
@EqualsAndHashCode(callSuper = true)
public class SyncCheckReq extends BaseRequest<SyncCheckReq, SyncCheckResp> {

    private String sId;

    private String sKey;

    private String uin;

    private String deviceId;

    private SyncKey syncKey;

    private long loginTime;

    public SyncCheckReq(String uri) {
        super(uri);
    }

    public SyncCheckReq setSId(String sId) {
        this.sId = sId;
        return this;
    }

    public SyncCheckReq setSKey(String sKey) {
        this.sKey = sKey;
        return this;
    }

    public SyncCheckReq setUin(String uin) {
        this.uin = uin;
        return this;
    }

    public SyncCheckReq setDeviceId(String deviceId) {
        this.deviceId = deviceId;
        return this;
    }

    public SyncCheckReq setSyncKey(SyncKey syncKey) {
        this.syncKey = syncKey;
        return this;
    }

    public SyncCheckReq setLoginTime(long loginTime) {
        this.loginTime = loginTime;
        return this;
    }

    @Override
    public SyncCheckReq build() {

        Map<String, String> headerMap = getHeaderMap();
        headerMap.put(WXHeaderKey.USER_AGENT, DefaultConfig.USER_AGENT);

        Map<String, Object> requestParamMap = getRequestParamMap();
        requestParamMap.put(WXQueryKey.R, BaseUtil.getEpochSecond() * 1000);
        requestParamMap.put(WXQueryKey.SKEY, sKey);
        requestParamMap.put(WXQueryKey.SID, sId);
        requestParamMap.put(WXQueryKey.UIN, uin);
        requestParamMap.put(WXQueryKey.DEVICE_ID, deviceId);
        requestParamMap.put(WXQueryKey.SYNC_KEY, syncKey.toString());
        requestParamMap.put(StringPool.UNDERSCORE, loginTime);

        return this;
    }

    @Override
    public SyncCheckResp convertRespBodyToObj(String strEntity) {
        Matcher matcher = Pattern
                .compile("window.synccheck=\\{retcode:\"(\\d*)\",selector:\"(\\d*)\"}")
                .matcher(strEntity);
        if (matcher.find()) {
            return new SyncCheckResp(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)));
        }
        log.error("同步检查响应消息解析失败:{}", strEntity);
        return null;
    }
}
