package pers.hll.aigc4chat.common.entity.wechat.message;

import jakarta.xml.bind.annotation.*;
import lombok.Data;

/**
 * 暂时无法转化
 * "&lt;msg&gt;&lt;emoji fromusername=\\\"xxx\\\" tousername=\\\"xxx\\\" type=\\\"1\\\" idbuffer=\\\"media:0_0\\\" md5=\\\"9e3f303561566dc9342a3ea41e6552a6\\\" len=\\\"8178\\\" productid=\\\"\\\" androidmd5=\\\"9e3f303561566dc9342a3ea41e6552a6\\\" androidlen=\\\"8178\\\" s60v3md5=\\\"9e3f303561566dc9342a3ea41e6552a6\\\" s60v3len=\\\"8178\\\" s60v5md5=\\\"9e3f303561566dc9342a3ea41e6552a6\\\" s60v5len=\\\"8178\\\" cdnurl=\\\"http://wxapp.tc.qq.com/262/20304/stodownload?m=9e3f303561566dc9342a3ea41e6552a6&amp;amp;filekey=30340201010420301e020201060402534804109e3f303561566dc9342a3ea41e6552a602021ff2040d00000004627466730000000132&amp;amp;hy=SH&amp;amp;storeid=2631a89f200019b53000000000000010600004f5053482e3c596096ad99f5a&amp;amp;bizid=1023\\\" designerid=\\\"\\\" thumburl=\\\"\\\" encrypturl=\\\"http://wxapp.tc.qq.com/262/20304/stodownload?m=d49180bb4744ce090e00640aaea4ddf2&amp;amp;filekey=30340201010420301e02020106040253480410d49180bb4744ce090e00640aaea4ddf202022000040d00000004627466730000000132&amp;amp;hy=SH&amp;amp;storeid=2631a89f200033569000000000000010600004f50534828fc596096ad76057&amp;amp;bizid=1023\\\" aeskey=\\\"b8e0b4f14c83e60f41f72baac6d13091\\\" externurl=\\\"http://wxapp.tc.qq.com/262/20304/stodownload?m=283bb6dbf84ba274ad7312a26117d5f3&amp;amp;filekey=30340201010420301e02020106040253480410283bb6dbf84ba274ad7312a26117d5f302020870040d00000004627466730000000132&amp;amp;hy=SH&amp;amp;storeid=2631a89f20005d114000000000000010600004f5053481166fb40b6b7891c8&amp;amp;bizid=1023\\\" externmd5=\\\"5ce491fed083095e5e5f2eee88509773\\\" width=\\\"100\\\" height=\\\"100\\\" tpurl=\\\"\\\" tpauthkey=\\\"\\\" attachedtext=\\\"\\\" attachedtextcolor=\\\"\\\" lensid=\\\"\\\" emojiattr=\\\"\\\" linkid=\\\"\\\" desc=\\\"\\\"&gt;&lt;/emoji&gt;&lt;gameext type=\\\"2\\\" content=\\\"5\\\"&gt;&lt;/gameext&gt;&lt;/msg&gt;\""
 *
 */
@Data
@XmlRootElement(name = "msg")
@XmlAccessorType(XmlAccessType.FIELD)
public class EmojiMsg {
  
    @XmlElement(name = "emoji")
    private Emoji emoji;  
  
    @XmlElement(name = "gameext")  
    private GameExt gameExt;

    @Data
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Emoji {

        @XmlAttribute(name = "fromusername")
        private String fromUserName;
  
        @XmlAttribute(name = "tousername")  
        private String toUserName;
  
        @XmlAttribute(name = "type")  
        private int type;  
  
        @XmlAttribute(name = "idbuffer")  
        private String idBuffer;
  
        @XmlAttribute(name = "md5")  
        private String md5;  
  
        @XmlAttribute(name = "len")  
        private int len;  
  
        @XmlAttribute(name = "productid")  
        private String productId;
  
        @XmlAttribute(name = "androidmd5")  
        private String androidMd5;
  
        @XmlAttribute(name = "androidlen")
        private int androidLen;
  
        @XmlAttribute(name = "s60v3md5")  
        private String s60v3md5;  
  
        @XmlAttribute(name = "s60v3len")  
        private int s60v3len;  
  
        @XmlAttribute(name = "s60v5md5")  
        private String s60v5md5;  
  
        @XmlAttribute(name = "s60v5len")  
        private int s60v5len;  
  
        @XmlAttribute(name = "cdnurl")  
        private String cdnUrl;
  
        @XmlAttribute(name = "designerid")  
        private String designerId;
  
        @XmlAttribute(name = "thumburl")  
        private String thumbUrl;
  
        @XmlAttribute(name = "encrypturl")  
        private String encryptUrl;
  
        @XmlAttribute(name = "aeskey")  
        private String aesKey;
  
        @XmlAttribute(name = "externurl")  
        private String externUrl;
  
        @XmlAttribute(name = "externmd5")  
        private String externMd5;
  
        @XmlAttribute(name = "width")  
        private int width;  
  
        @XmlAttribute(name = "height")  
        private int height;  
  
        @XmlAttribute(name = "tpurl")  
        private String tpUrl;
  
        @XmlAttribute(name = "tpauthkey")  
        private String tpAuthKey;
  
        @XmlAttribute(name = "attachedtext")  
        private String attachedText;
  
        @XmlAttribute(name = "attachedtextcolor")  
        private String attachedTextColor;
  
        @XmlAttribute(name = "lensid")  
        private String lenSid;
  
        @XmlAttribute(name = "emojiattr")  
        private String emojiAttr;
  
        @XmlAttribute(name = "linkid")  
        private String linkId;
  
        @XmlAttribute(name = "desc")  
        private String desc;
    }  

    @Data
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class GameExt {

        @XmlAttribute(name = "type")  
        private int type;  
  
        @XmlAttribute(name = "content")  
        private String content;
    }
}