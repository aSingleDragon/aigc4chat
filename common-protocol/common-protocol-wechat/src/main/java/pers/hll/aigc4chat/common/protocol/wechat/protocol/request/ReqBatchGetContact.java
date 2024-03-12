package pers.hll.aigc4chat.common.protocol.wechat.protocol.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * 批量获取联系人
 *
 * @author hll
 * @author 2024/03/10
 */
public class ReqBatchGetContact {

    @JsonProperty("BaseRequest")
    public final BaseRequest baseRequest;

    @JsonProperty("Count")
    public final int count;

    @JsonProperty("List")
    public final List<Contact> list;

    public ReqBatchGetContact(BaseRequest baseRequest, List<Contact> contacts) {
        this.baseRequest = baseRequest;
        this.count = contacts.size();
        this.list = contacts;
    }

    public static class Contact {

        @JsonProperty("UserName")
        public final String userName;

        @JsonProperty("EncryChatRoomId")
        public final String encryChatRoomId;

        public Contact(String userName, String encryChatRoomId) {
            this.userName = userName;
            this.encryChatRoomId = encryChatRoomId;
        }
    }
}
