package pers.hll.aigc4chat.common.protocol.wechat.protocol.response;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @author hll
 * @author 2024/03/10
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WebWxUploadMediaResp extends BaseResponseBaseBody {

    @SerializedName("MediaId")
    private String mediaId;

    @SerializedName("StartPos")
    private long startPos;

    @SerializedName("CDNThumbImgHeight")
    private int cdnThumbImgHeight;

    @SerializedName("CDNThumbImgWidth")
    private int cdnThumbImgWidth;
}
