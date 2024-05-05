package pers.hll.aigc4chat.protocol.wechat.response;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 撤回消息 响应 body
 *
 * @author hll
 * @author 2024/03/10
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WebWxRevokeMsgResp extends BaseResponseBaseBody {

    @SerializedName("Introduction")
    public String introduction;

    @SerializedName("SysWording")
    public String sysWording;
}
