package pers.hll.aigc4chat.common.entity.wechat.message;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.File;
import java.io.Serializable;

/**
 * 微信语音消息
 *
 * @author hll
 * @since 2024/03/10
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WXVoice extends WXMessage implements Serializable, Cloneable {

    /**
     * 语音长度，毫秒
     */
    private long voiceLength;

    /**
     * 语音文件
     */
    private File voice;

    @Override
    public WXVoice clone() {
        return (WXVoice) super.clone();
    }
}
