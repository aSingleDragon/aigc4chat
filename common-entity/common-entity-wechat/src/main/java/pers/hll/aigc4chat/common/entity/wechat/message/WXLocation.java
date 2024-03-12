package pers.hll.aigc4chat.common.entity.wechat.message;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 微信位置消息
 *
 * @author hll
 * @since 2024/03/10
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WXLocation extends WXMessage implements Serializable, Cloneable {

    /**
     * 地点名称
     */
    private String locationName;

    /**
     * 地点地图图片
     */
    private String locationImage;

    /**
     * 地点的腾讯地图url
     */
    private String locationUrl;

    @Override
    public WXLocation clone() {
        return (WXLocation) super.clone();
    }
}
