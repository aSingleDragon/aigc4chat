package pers.hll.aigc4chat.common.protocol.wechat.protocol.request;

import lombok.RequiredArgsConstructor;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.request.BaseRequest;

import java.util.ArrayList;

/**
 *
 * @author hll
 * @author 2024/03/10
 */
public class ReqVerifyUser {

    public pers.hll.aigc4chat.common.protocol.wechat.protocol.request.BaseRequest BaseRequest;

    public int Opcode;

    public int VerifyUserListSize;

    public ArrayList<VerifyUser> VerifyUserList;

    public String VerifyContent;

    public int SceneListCount;

    public ArrayList<Integer> SceneList;

    public String skey;

    public ReqVerifyUser(
            BaseRequest baseRequest, int opCode, String userName, String verifyTicket, String verifyContent) {
        this.BaseRequest = baseRequest;
        this.Opcode = opCode;
        this.VerifyUserListSize = 1;
        this.VerifyUserList = new ArrayList<>();
        this.VerifyUserList.add(new VerifyUser(userName, verifyTicket));
        this.VerifyContent = verifyContent;
        this.SceneListCount = 1;
        this.SceneList = new ArrayList<>();
        this.SceneList.add(33);
        this.skey = baseRequest.sKey;
    }

    @RequiredArgsConstructor
    public static class VerifyUser {

        public final String Value;

        public final String VerifyUserTicket;
    }
}
