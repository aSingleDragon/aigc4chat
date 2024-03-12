package pers.hll.aigc4chat.common.entity.wechat.message;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 微信撤回消息
 *
 * @author hll
 * @since 2024/03/10
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WXRevoke extends WXMessage implements Serializable, Cloneable {

    /**
     * 撤回消息id
     */
    private long msgId;

    /**
     * 撤回消息提示文字
     */
    private String msgReplace;

    @Override
    public WXRevoke clone() {
        return (WXRevoke) super.clone();
    }
}
