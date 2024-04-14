package pers.hll.aigc4chat.common.entity.wechat.message;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 微信链接消息
 *
 * @author hll
 * @since 2024/03/10
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WXLink extends WXMessage {

    /**
     * 链接标题
     */
    private String linkName;

    /**
     * 链接地址
     */
    private String linkUrl;
}
