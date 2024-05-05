package pers.hll.aigc4chat.protocol.wechat.request.body;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import pers.hll.aigc4chat.base.util.BaseUtil;

/**
 * 消息 body
 *
 * @author hll
 * @since 2024/03/19
 */
@Data
@Builder
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

    /**
     * 构造方法
     * TODO 后面有时间把这个构造方法删掉用builder去构造实例对象
     *
     * @param type          消息类型
     * @param mediaId       媒体ID
     * @param emojiFlag     表情标志
     * @param content       消息内容
     * @param signature     签名
     * @param fromUserName  发送者
     * @param toUserName    接收者
     */
    @Tolerate
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