package pers.hll.aigc4chat.protocol.wechat.request;

import lombok.EqualsAndHashCode;
import pers.hll.aigc4chat.protocol.wechat.constant.DefaultConfig;
import pers.hll.aigc4chat.protocol.wechat.constant.WXHeaderKey;
import pers.hll.aigc4chat.protocol.wechat.constant.WXQueryKey;

import java.util.Map;

/**
 * 获取图片请求
 *
 * @author hll
 * @since 2024/03/11
 */
@EqualsAndHashCode(callSuper = true)
public class WebWxGetMsgImgReq extends BaseRequest<WebWxGetMsgImgReq, Object> {

    private long msgId;

    private String sKey;

    public WebWxGetMsgImgReq(String uri) {
        super(uri);
    }

    public WebWxGetMsgImgReq setMsgId(long msgId) {
        this.msgId = msgId;
        return this;
    }

    public WebWxGetMsgImgReq setSKey(String sKey) {
        this.sKey = sKey;
        return this;
    }

    @Override
    public WebWxGetMsgImgReq build() {

        Map<String, String> headerMap = getHeaderMap();
        headerMap.put(WXHeaderKey.USER_AGENT, DefaultConfig.USER_AGENT);

        Map<String, Object> requestParamMap = getRequestParamMap();
        requestParamMap.put(WXQueryKey.MSG_ID, String.valueOf(msgId));
        requestParamMap.put(WXQueryKey.SKEY, sKey);
        return this;
    }

    @Override
    public WebWxGetMsgImgReq setFileStreamAvailable(boolean fileStreamAvailable) {
        this.fileStreamAvailable = true;
        return this;
    }
}
