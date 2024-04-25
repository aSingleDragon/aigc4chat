package pers.hll.aigc4chat.protocol.wechat.request.body;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 检查文件是否已经在微信服务器上请求的 body
 *
 * @author hll
 * @author 2024/03/10
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class WebWxCheckUploadReqBody extends BasePostRequestBaseBody {

    @SerializedName("FileMd5")
    private String fileMd5;

    @SerializedName("FileName")
    private String fileName;

    @SerializedName("FileSize")
    private long fileSize;

    @SerializedName("FileType")
    private int fileType;

    @SerializedName("FromUserName")
    private String fromUserName;

    @SerializedName("ToUserName")
    private String toUserName;
}
