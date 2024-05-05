package pers.hll.aigc4chat.protocol.wechat.request.body;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 联系 body
 *
 * @author hll
 * @since 2024/03/19
 */
@Data
@AllArgsConstructor
public class Contact {

    public Contact(String userName) {
        this.userName = userName;
        this.encryptChatRoomId = "";
    }

    @SerializedName("UserName")
    public final String userName;

    @SerializedName("EncryChatRoomId")
    public final String encryptChatRoomId;
}