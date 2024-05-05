package pers.hll.aigc4chat.entity.wechat.message;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 微信静态或动态图片消息
 *
 * @author hll
 * @since 2024/03/10
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WXImage extends WXMessage {

    /**
     * 图片宽度
     */
    private int imgWidth;

    /**
     * 图片高度
     */
    private int imgHeight;

    /**
     * 静态图消息中的缩略图，动态图消息中的原图
     */
    private String image;

    /**
     * 静态图获取原图之前为null，获取原图之后为原图，动态图一开始就是原图
     */
    private String origin;
}
