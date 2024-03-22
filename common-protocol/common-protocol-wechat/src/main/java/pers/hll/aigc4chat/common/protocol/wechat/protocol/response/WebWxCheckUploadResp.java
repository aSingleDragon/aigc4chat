package pers.hll.aigc4chat.common.protocol.wechat.protocol.response;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *  检查文件是否已经上传的响应 body
 *
 * @author hll
 * @author 2024/03/10
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WebWxCheckUploadResp extends BaseResponseBaseBody {

    @SerializedName("AESKey")
    private String aseKey;

    @SerializedName("Signature")
    private String signature;

    @SerializedName("MediaId")
    private String mediaId;

    @SerializedName("EncryFileName")
    private String encryFileName;
}
