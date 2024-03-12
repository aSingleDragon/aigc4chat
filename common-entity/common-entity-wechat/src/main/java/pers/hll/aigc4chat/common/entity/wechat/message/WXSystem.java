package pers.hll.aigc4chat.common.entity.wechat.message;

import java.io.Serializable;

/**
 * 微信系统消息，新人入群，被踢出群，红包消息等
 *
 * @author hll
 * @since 2024/03/10
 */
public class WXSystem extends WXMessage implements Serializable, Cloneable {
    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public WXSystem clone() {
        return (WXSystem) super.clone();
    }
}
