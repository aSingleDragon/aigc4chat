package pers.hll.aigc4chat.common.protocol.wechat.protocol.request;

import pers.hll.aigc4chat.common.protocol.wechat.protocol.request.BaseRequest;

/**
 *
 * {
 *     "Sid": "Ff8wJtPIRc6boqsw",
 *     "Skey": "@crypt_8b7318fd_10f3dd566efbd78875113e55b685a7cf",
 *     "Uin": "2977348135"
 * }
 *
 * @author hll
 * @author 2024/03/10
 */
public class ReqInit {

    public final pers.hll.aigc4chat.common.protocol.wechat.protocol.request.BaseRequest BaseRequest;

    public ReqInit(BaseRequest baseRequest) {
        this.BaseRequest = baseRequest;
    }
}
