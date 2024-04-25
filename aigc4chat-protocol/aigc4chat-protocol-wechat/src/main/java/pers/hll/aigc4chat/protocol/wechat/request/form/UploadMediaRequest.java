package pers.hll.aigc4chat.protocol.wechat.request.form;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import pers.hll.aigc4chat.protocol.wechat.request.body.BasePostRequestBaseBody;

/**
 * uploadmediarequest
 *
 * @author hll
 * @author 2024/03/10
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UploadMediaRequest extends BasePostRequestBaseBody {

    @SerializedName("UploadType")
    private int uploadType;

    @SerializedName("ClientMediaId")
    private long clientMediaId;

    @SerializedName("TotalLen")
    private long totalLen;

    @SerializedName("StartPos")
    private long startPos;

    @SerializedName("DataLen")
    private long dataLen;

    @SerializedName("MediaType")
    private int mediaType;

    @SerializedName("FromUserName")
    private String fromUserName;

    @SerializedName("ToUserName")
    private String toUserName;

    @SerializedName("FileMd5")
    private String fileMd5;

    @SerializedName("AESKey")
    private String aesKey;

    @SerializedName("Signature")
    private String signature;
}
