package pers.hll.aigc4chat.entity.wechat.message;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 微信好友请求消息
 *
 * @author hll
 * @since 2024/03/10
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WXVerify extends WXMessage {

    /**
     * 请求用户id
     */
    private String userId;

    /**
     * 请求用户名称
     */
    private String userName;

    /**
     * 请求用户个性签名
     */
    private String signature;

    /**
     * 请求用户所在省份
     */
    private String province;

    /**
     * 请求用户所在城市
     */
    private String city;

    /**
     * 请求用户性别
     */
    private int gender;

    /**
     * 请求用户验证标志位
     */
    private int verifyFlag;

    /**
     * 请求用户票据
     */
    private String ticket;
}
