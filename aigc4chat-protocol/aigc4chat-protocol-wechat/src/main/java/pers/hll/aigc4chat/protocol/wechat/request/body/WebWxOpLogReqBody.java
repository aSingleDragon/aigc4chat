package pers.hll.aigc4chat.protocol.wechat.request.body;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 微信修改个人资料请求 body
 *
 * @author hll
 * @author 2024/03/10
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class WebWxOpLogReqBody extends BasePostRequestBaseBody {

    @SerializedName("CmdId")
    private int cmdId;

    @SerializedName("OP")
    private int op;

    @SerializedName("UserName")
    private String userName;

    @SerializedName("RemarkName")
    private String remarkName;
}
