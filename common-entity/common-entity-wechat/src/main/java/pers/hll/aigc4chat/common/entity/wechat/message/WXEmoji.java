package pers.hll.aigc4chat.common.entity.wechat.message;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 微信表情商店表情消息，该类型的消息无法下载图片
 *
 * @author hll
 * @since 2024/03/10
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WXEmoji extends WXMessage implements Serializable, Cloneable {

    /**
     * 图片宽度
     */
    private int imgWidth;

    /**
     * 图片高度
     */
    private int imgHeight;

    @Override
    public WXEmoji clone() {
        return (WXEmoji) super.clone();
    }
}
