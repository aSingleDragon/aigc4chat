package pers.hll.aigc4chat.entity.wechat.message;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 微信状态消息
 *
 * @author hll
 * @since 2024/03/10
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WXNotify extends WXMessage {

    public static final int NOTIFY_READED = 1;

    public static final int NOTIFY_ENTER_SESSION = 2;

    public static final int NOTIFY_INITED = 3;

    public static final int NOTIFY_SYNC_CONV = 4;

    public static final int NOTIFY_QUIT_SESSION = 5;

    /**
     * 状态码
     */
    private int notifyCode;

    /**
     * 关联联系人
     */
    private String notifyContact;
}
