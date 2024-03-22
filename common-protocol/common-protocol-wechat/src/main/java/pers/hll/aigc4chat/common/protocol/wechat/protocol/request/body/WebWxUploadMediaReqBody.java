package pers.hll.aigc4chat.common.protocol.wechat.protocol.request.body;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.File;

/**
 * 上传文件请求 body
 *
 * @author hll
 * @author 2024/03/10
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class WebWxUploadMediaReqBody extends BasePostRequestBaseBody {

    private String id;

    private String name;

    private String type;

    private String lastModifiedDate;

    private long size;

    @SerializedName("mediatype")
    private String mediaType;

    @SerializedName("uploadmediarequest")
    private String uploadMediaRequest;

    @SerializedName("webwx_data_ticket")
    private String webWxDataTicket;

    @SerializedName("pass_ticket")
    private String passTicket;

    @SerializedName("filename")
    private File file;

}
