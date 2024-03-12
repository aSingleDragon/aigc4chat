package pers.hll.aigc4chat.common.entity.wechat.message;

import lombok.Data;
import pers.hll.aigc4chat.common.entity.wechat.contact.WXContact;
import pers.hll.aigc4chat.common.entity.wechat.contact.WXGroup;
import pers.hll.aigc4chat.common.entity.wechat.contact.WXUser;

import java.io.Serializable;

/**
 * 微信消息
 *
 * @author hll
 * @since 2024/03/10
 */
@Data
public abstract class WXMessage implements Serializable, Cloneable {

    /**
     * 消息的id
     */
    protected long id;

    /**
     * 消息的本地id
     */
    protected long idLocal;

    /**
     * 消息的时间戳
     */
    protected long timestamp;

    /**
     * 消息来源的群，如果不是群消息，值为null
     */
    protected WXGroup fromGroup;

    /**
     * 消息来源的用户，如果不来自特定用户，值为null
     */
    protected WXUser fromUser;

    /**
     * 消息发送的联系人
     */
    protected WXContact toContact;

    /**
     * 消息的内容
     */
    protected String content;

    @Override
    public WXMessage clone() {
        try {
            WXMessage wxMessage = (WXMessage) super.clone();
            if (wxMessage.fromGroup != null) {
                wxMessage.fromGroup = this.fromGroup.clone();
            }
            if (wxMessage.fromUser != null) {
                wxMessage.fromUser = this.fromUser.clone();
            }
            if (wxMessage.toContact != null) {
                wxMessage.toContact = this.toContact.clone();
            }
            return wxMessage;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            throw new IllegalStateException();
        }
    }
}
