package pers.hll.aigc4chat.common.protocol.wechat.protocol.request;

import pers.hll.aigc4chat.common.protocol.wechat.protocol.request.BaseRequest;

/**
 *
 * @author hll
 * @author 2024/03/10
 */
public class ReqRevokeMsg {

    public pers.hll.aigc4chat.common.protocol.wechat.protocol.request.BaseRequest BaseRequest;

    public String ClientMsgId;

    public String SvrMsgId;

    public String ToUserName;

    public ReqRevokeMsg(BaseRequest baseRequest, String clientMsgId, String serverMsgId, String toUserName) {
        this.BaseRequest = baseRequest;
        this.ClientMsgId = clientMsgId;
        this.SvrMsgId = serverMsgId;
        this.ToUserName = toUserName;
    }
}
