package pers.hll.aigc4chat.common.protocol.wechat.protocol.request;

import pers.hll.aigc4chat.common.protocol.wechat.protocol.request.BaseRequest;

/**
 *
 * @author hll
 * @author 2024/03/10
 */
public class ReqUpdateChatroom {

    public pers.hll.aigc4chat.common.protocol.wechat.protocol.request.BaseRequest BaseRequest;

    public String ChatRoomName;

    public String NewTopic;

    public String AddMemberList;

    public String DelMemberList;

    public ReqUpdateChatroom(BaseRequest baseRequest, String chatroomName, String fun, String name, String members) {
        this.BaseRequest = baseRequest;
        this.ChatRoomName = chatroomName;
        this.NewTopic = name;
        switch (fun) {
            case "addmember":
                this.AddMemberList = members;
                break;
            case "delmember":
                this.DelMemberList = members;
                break;
        }
    }
}
