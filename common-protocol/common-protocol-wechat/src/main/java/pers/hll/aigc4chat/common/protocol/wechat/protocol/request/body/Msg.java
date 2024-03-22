package pers.hll.aigc4chat.common.protocol.wechat.protocol.request.body;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import pers.hll.aigc4chat.common.base.util.BaseUtil;

/**
 * 消息 body
 *
 * @author hll
 * @since 2024/03/19
 */
@Data
public class Msg {

    @SerializedName("Type")
    private int type;

    @SerializedName("EmojiFlag")
    private Integer emojiFlag;

    @SerializedName("MediaId")
    private String mediaId;

    @SerializedName("Content")
    private String content;

    @SerializedName("Signature")
    private String signature;

    @SerializedName("FromUserName")
    private String fromUserName;

    @SerializedName("ToUserName")
    private String toUserName;

    @SerializedName("LocalID")
    private String localId;

    @SerializedName("ClientMsgId")
    private String clientMsgId;

    public Msg(int type, String mediaId, Integer emojiFlag, String content, String signature, String fromUserName,
               String toUserName) {
        this.type = type;
        this.mediaId = mediaId;
        this.emojiFlag = emojiFlag;
        this.content = content;
        this.signature = signature;
        this.fromUserName = fromUserName;
        this.toUserName = toUserName;
        this.localId = BaseUtil.createMsgId();
        this.clientMsgId = localId;
    }
}