package pers.hll.aigc4chat.common.protocol.wechat.protocol.request.body;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * 联系 body
 *
 * @author hll
 * @since 2024/03/19
 */
@Data
public class Contact {

    @SerializedName("UserName")
    public final String userName;

    @SerializedName("EncryChatRoomId")
    public final String encryChatRoomId;
}