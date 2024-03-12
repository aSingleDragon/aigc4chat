package pers.hll.aigc4chat.common.protocol.wechat.protocol.request;

import pers.hll.aigc4chat.common.protocol.wechat.protocol.request.BaseRequest;

/**
 *
 * @author hll
 * @author 2024/03/10
 */
public class ReqStatusNotify {

    public pers.hll.aigc4chat.common.protocol.wechat.protocol.request.BaseRequest BaseRequest;

    public int Code;

    public String FromUserName;

    public String ToUserName;

    public long ClientMsgId;

    public ReqStatusNotify(BaseRequest baseRequest, int code, String myName) {
        this.BaseRequest = baseRequest;
        this.Code = code;
        this.FromUserName = myName;
        this.ToUserName = myName;
        this.ClientMsgId = System.currentTimeMillis();
    }
}
