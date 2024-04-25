package pers.hll.aigc4chat.protocol.wechat.response;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 上传媒体文件 响应 body
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
