package pers.hll.aigc4chat.common.protocol.wechat.protocol.response;

import java.util.ArrayList;

/**
 *
 * @author hll
 * @author 2024/03/10
 */
public class RspBatchGetContact {

    public pers.hll.aigc4chat.common.protocol.wechat.protocol.response.BaseResponse BaseResponse;

    public int Count;

    public ArrayList<RspInit.User> ContactList;
}
