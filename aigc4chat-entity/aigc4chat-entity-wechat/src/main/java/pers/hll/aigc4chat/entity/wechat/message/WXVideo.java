package pers.hll.aigc4chat.entity.wechat.message;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 微信视频消息
 *
 * @author hll
 * @since 2024/03/10
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WXVideo extends WXMessage {

    /**
     * 视频缩略图宽度
     */
    private int imgWidth;

    /**
     * 视频缩略图高度
     */
    private int imgHeight;

    /**
     * 视频的长度，秒
     */
    private int videoLength;

    /**
     * 视频缩略图
     */
    private String image;

    /**
     * 视频文件
     */
    private String video;
}
