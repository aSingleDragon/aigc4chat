package pers.hll.aigc4chat.common.protocol.wechat.protocol.response;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.response.webwxinit.User;

import java.util.List;

/**
 *
 * @author hll
 * @author 2024/03/14
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WebWxStatusNotifyResp extends BaseResponseBaseBody {

    @SerializedName("MsgID")
    public String msgID;
}
