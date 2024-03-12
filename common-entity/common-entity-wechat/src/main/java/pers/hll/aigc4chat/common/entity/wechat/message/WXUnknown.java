package pers.hll.aigc4chat.common.entity.wechat.message;

import java.io.Serializable;

/**
 * 微信未知类型消息
 *
 * @author hll
 * @since 2024/03/10
 */
public class WXUnknown extends WXMessage implements Serializable, Cloneable {
    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public WXUnknown clone() {
        return (WXUnknown) super.clone();
    }
}
