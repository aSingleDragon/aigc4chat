package pers.hll.aigc4chat.common.protocol.wechat.protocol.request.body;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Contact {

    @SerializedName("UserName")
    public final String userName;

    @SerializedName("EncryChatRoomId")
    public final String encryChatRoomId;
}