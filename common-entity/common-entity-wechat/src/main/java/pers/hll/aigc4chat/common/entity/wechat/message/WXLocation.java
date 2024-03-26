package pers.hll.aigc4chat.common.entity.wechat.message;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 微信位置消息
 *
 * @author hll
 * @since 2024/03/10
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WXLocation extends WXMessage {

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
     * <a href="http://apis.map.qq.com/uri/v1/geocoder?coord=30.586099,104.049659">URL 示例</a>
     */
    private String locationUrl;

    private OriContent oriContent;
}
