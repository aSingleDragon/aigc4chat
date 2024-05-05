import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pers.hll.aigc4chat.base.util.StringUtil;
import pers.hll.aigc4chat.base.util.XmlUtil;
import pers.hll.aigc4chat.entity.wechat.message.AppMsg;
import pers.hll.aigc4chat.entity.wechat.message.EmojiMsg;
import pers.hll.aigc4chat.entity.wechat.message.OriContent;
import pers.hll.aigc4chat.entity.wechat.message.VoiceMsg;

import java.io.IOException;
import java.util.List;

public class UtilTest {
    public static void main(String[] args) throws IOException {
        //String fileName = "voice.amr"; // 通常为".amr"格式，也可能是其他格式，根据实际获取的语音文件格式确定
        //String voice = "@bbfb9723454802912492c2f89403a7d6ed0fa54e7e16e8104fac5f45d173f26b3388caf0c103c729a59b875e5123dab0375bdcc6d5df27d321f2bd2c5613800a8b5a42f90389f1aa793b3219fbb56858b1c6809036705f794596268f114f6cffbdbc8fe8f7b41ad2b911bbf3b9c1c16c1694694ccf2ce1bca64b3e05ef710add34e4306534434d8c55ca115f2f12a44ad4daaf64d8309d7ac15ca9370f71d1990f21c67aac881b0646dbdd29d1dbd18a4091ceb57d5f5e4ccee0f170fa60cba61e68ada724a547c79c83e6aad5a3ee6955844f953e94d23cc13308cf61d8eefe8116ad95dd0a1df9a27fa32139837808a9fe2b60f3bf6e6716e99002c9a8584df98491d02600a0d17bbf12e19f08064e1484c78d39d58a471bf585acdbf6a72e9fc4f78f0164ca42b451115a6d0f192184f5c37be6d7764bbddcf75646ac4bb195591f204591bc83b264a7257b55b3d1f76570f13811c1fd912ea371e86759cb7191ca76951fc818642e524834300752aa9fe23df46fe3a13ccc5fab88dc8c323f738ed790ddd3878a35a778008cfa6247d8a1f32c2a91ea6b99f84ddc2a8a25117cd4d5735734a28b11a7ec6bc73545acb54b53d97810a3fc650dbb4d02ec6de8062299b91832e6c1a0417f5c005d42fca801c7be14fad9b7026be59d14a8d5bd39300eedcf9e2ef116f797593ee9f95e63b62786708cee6feabcb4b236f1efa2dc6ff4ca602b2cd954fe0997addd11";
        //// 将二进制数据写入文件
        //try (FileOutputStream fos = new FileOutputStream(fileName)) {
        //    fos.write(voice.getBytes());
        //} catch (IOException e) {
        //    e.printStackTrace();
        //}

        String emojo = "&lt;msg&gt;&lt;emoji fromusername=\\\"xxx\\\" tousername=\\\"xxx\\\" type=\\\"1\\\" idbuffer=\\\"media:0_0\\\" md5=\\\"9e3f303561566dc9342a3ea41e6552a6\\\" len=\\\"8178\\\" productid=\\\"\\\" androidmd5=\\\"9e3f303561566dc9342a3ea41e6552a6\\\" androidlen=\\\"8178\\\" s60v3md5=\\\"9e3f303561566dc9342a3ea41e6552a6\\\" s60v3len=\\\"8178\\\" s60v5md5=\\\"9e3f303561566dc9342a3ea41e6552a6\\\" s60v5len=\\\"8178\\\" cdnurl=\\\"http://wxapp.tc.qq.com/262/20304/stodownload?m=9e3f303561566dc9342a3ea41e6552a6&amp;amp;filekey=30340201010420301e020201060402534804109e3f303561566dc9342a3ea41e6552a602021ff2040d00000004627466730000000132&amp;amp;hy=SH&amp;amp;storeid=2631a89f200019b53000000000000010600004f5053482e3c596096ad99f5a&amp;amp;bizid=1023\\\" designerid=\\\"\\\" thumburl=\\\"\\\" encrypturl=\\\"http://wxapp.tc.qq.com/262/20304/stodownload?m=d49180bb4744ce090e00640aaea4ddf2&amp;amp;filekey=30340201010420301e02020106040253480410d49180bb4744ce090e00640aaea4ddf202022000040d00000004627466730000000132&amp;amp;hy=SH&amp;amp;storeid=2631a89f200033569000000000000010600004f50534828fc596096ad76057&amp;amp;bizid=1023\\\" aeskey=\\\"b8e0b4f14c83e60f41f72baac6d13091\\\" externurl=\\\"http://wxapp.tc.qq.com/262/20304/stodownload?m=283bb6dbf84ba274ad7312a26117d5f3&amp;amp;filekey=30340201010420301e02020106040253480410283bb6dbf84ba274ad7312a26117d5f302020870040d00000004627466730000000132&amp;amp;hy=SH&amp;amp;storeid=2631a89f20005d114000000000000010600004f5053481166fb40b6b7891c8&amp;amp;bizid=1023\\\" externmd5=\\\"5ce491fed083095e5e5f2eee88509773\\\" width=\\\"100\\\" height=\\\"100\\\" tpurl=\\\"\\\" tpauthkey=\\\"\\\" attachedtext=\\\"\\\" attachedtextcolor=\\\"\\\" lensid=\\\"\\\" emojiattr=\\\"\\\" linkid=\\\"\\\" desc=\\\"\\\"&gt;&lt;/emoji&gt;&lt;gameext type=\\\"2\\\" content=\\\"5\\\"&gt;&lt;/gameext&gt;&lt;/msg&gt;";
        String str = emojo
                .replace("\\", "")
                .replace("&lt;", "<")
                .replace("&gt;", ">");
                //.replace("&amp;amp;", "&")
                //.replace("&amp;", "&");
        EmojiMsg emojiMsg = XmlUtil.xmlStrToObject(str, EmojiMsg.class);
        System.out.println(emojiMsg);


        //System.out.println(ImageTypeUtils.typeOf("./file/image-slave-5585288290048795939.unk"));
    }

    @Test
    void testSplit() {
        String str = "name";
        List<String> list = StringUtil.splitToList(str);
        Assertions.assertNotNull(list);
        Assertions.assertFalse(list.isEmpty());
        Assertions.assertEquals(list.get(0), str);
        Assertions.assertEquals(1, list.size());
    }

    @Test
    void testAppMsg() {
        AppMsg appMsg = new AppMsg();
        appMsg.setTitle("哈哈哈.mp3");
        appMsg.setAppAttach(new AppMsg.AppAttach(123L, "1312", "mp3"));
        System.out.println(XmlUtil.objectToXmlStr(appMsg));
    }

    @Test
    void testLocationMsg() {
        OriContent.Location location = new OriContent.Location();
        location.setX(123.123);
        location.setY(123.123);
        location.setLabel("hll");
        location.setPoiName("123");
        OriContent oriContent = new OriContent();
        oriContent.setLocation(location);
        System.out.println(XmlUtil.objectToXmlStr(oriContent));
    }

    @Test
    void testVoiceMsg() {
        VoiceMsg voiceMsg = new VoiceMsg();
        voiceMsg.setVoice(new VoiceMsg.Voice("123"));
        voiceMsg.setMsgType("voice");
        voiceMsg.setToUserName("hll");
        voiceMsg.setCreateTime(12131L);
        voiceMsg.setFromUserName("hhah");
        System.out.println(XmlUtil.objectToXmlStr(voiceMsg));
    }
}
