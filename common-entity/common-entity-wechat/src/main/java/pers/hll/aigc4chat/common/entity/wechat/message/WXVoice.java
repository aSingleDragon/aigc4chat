package pers.hll.aigc4chat.common.entity.wechat.message;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 微信语音消息
 *
 * @author hll
 * @since 2024/03/10
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WXVoice extends WXMessage {

    /**
     * 语音长度，毫秒
     */
    private long voiceLength;

    /**
     * 语音文件
     */
    private String voice;

    private String mediaId;
}
