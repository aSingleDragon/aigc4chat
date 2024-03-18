package pers.hll.aigc4chat.common.protocol.wechat.protocol.response;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 *
 * @author hll
 * @author 2024/03/10
 */
@Data
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
