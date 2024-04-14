package pers.hll.aigc4chat.common.entity.wechat.message;

import jakarta.xml.bind.annotation.*;
import lombok.Data;

/**
 * 地址信息
 *
 * @author hll
 * @since 2024/03/22
 */
@Data
@XmlRootElement(name = "msg")
@XmlAccessorType(XmlAccessType.FIELD)
public class OriContent {

    @XmlElement(name = "location")
    private Location location;

    @Data
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Location {

        @XmlAttribute(name = "x")
        private double x;

        @XmlAttribute(name = "y")
        private double y;

        @XmlAttribute(name = "scale")
        private int scale;

        @XmlAttribute(name = "label")
        private String label;

        @XmlAttribute(name = "maptype")
        private String mapType;

        @XmlAttribute(name = "poiname")
        private String poiName;

        @XmlAttribute(name = "poiid")
        private String poiId;

        @XmlAttribute(name = "buildingId")
        private String buildingId;

        @XmlAttribute(name = "floorName")
        private String floorName;

        @XmlAttribute(name = "poiCategoryTips")
        private String poiCategoryTips;

        @XmlAttribute(name = "poiBusinessHour")
        private String poiBusinessHour;

        @XmlAttribute(name = "poiPhone")
        private String poiPhone;

        @XmlAttribute(name = "poiPriceTips")
        private String poiPriceTips;

        @XmlAttribute(name = "isFromPoiList")
        private boolean isFromPoiList;

        @XmlAttribute(name = "adcode")
        private String adCode;

        @XmlAttribute(name = "cityname")
        private String cityName;
    }
}
