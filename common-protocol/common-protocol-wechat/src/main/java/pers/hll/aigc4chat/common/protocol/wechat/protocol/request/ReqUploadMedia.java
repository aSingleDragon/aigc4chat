package pers.hll.aigc4chat.common.protocol.wechat.protocol.request;

import pers.hll.aigc4chat.common.protocol.wechat.protocol.request.BaseRequest;

import java.util.Random;

/**
 *
 * @author hll
 * @author 2024/03/10
 */
public class ReqUploadMedia {

    public pers.hll.aigc4chat.common.protocol.wechat.protocol.request.BaseRequest BaseRequest;

    public long ClientMediaId;

    public int UploadType;

    public long TotalLen;

    public long StartPos;

    public long DataLen;

    public int MediaType;

    public String FromUserName;

    public String ToUserName;

    public String FileMd5;

    public String AESKey;

    public String Signature;

    public ReqUploadMedia(BaseRequest baseRequest, long clientMediaId, int uploadType, long totalLen, long startPos,
                          long dataLen, String fileMd5, String aesKey, String signature, String fromUserName, String toUserName) {
        this.BaseRequest = baseRequest;
        this.ClientMediaId = clientMediaId;
        this.UploadType = uploadType;
        this.TotalLen = totalLen;
        this.StartPos = startPos;
        this.DataLen = dataLen;
        this.FileMd5 = fileMd5;
        this.AESKey = aesKey;
        this.Signature = signature;
        this.MediaType = 4;
        this.FromUserName = fromUserName;
        this.ToUserName = toUserName;
    }

    public static long clientMediaId() {
        return System.currentTimeMillis() * 10000 + new Random().nextInt(10000);
    }
}
