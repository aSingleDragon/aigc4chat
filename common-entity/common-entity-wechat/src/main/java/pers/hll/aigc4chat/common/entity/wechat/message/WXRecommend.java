package pers.hll.aigc4chat.common.entity.wechat.message;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 微信名片消息
 *
 * @author hll
 * @since 2024/03/10
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WXRecommend extends WXMessage implements Serializable, Cloneable {

    /**
     * 名片用户id
     */
    private String userId;

    /**
     * 名片用户名称
     */
    private String userName;

    /**
     * 名片用户性别
     */
    private int gender;

    /**
     * 名片用户个性签名
     */
    private String signature;

    /**
     * 名片用户所在省份
     */
    private String province;

    /**
     * 名片用户所在城市
     */
    private String city;

    /**
     * 名片用户验证标志位
     */
    private int verifyFlag;

    @Override
    public WXRecommend clone() {
        return (WXRecommend) super.clone();
    }
}
