package pers.hll.aigc4chat.common.protocol.wechat.protocol.response;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.response.webwxinit.User;

import java.util.List;

/**
 * 批量获取联系人 响应 body
 *
 * @author hll
 * @author 2024/03/14
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WebWxBatchGetContactResp extends BaseResponseBaseBody {

    @SerializedName("Count")
    public int count;

    @SerializedName("ContactList")
    public List<User> contactList;
}
