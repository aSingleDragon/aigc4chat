package pers.hll.aigc4chat.protocol.wechat.request.body;


import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 批量获取联系请求 body
 *
 * @author hll
 * @since 2024/03/19
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class WebWxBatchGetContactReqBody extends BasePostRequestBaseBody {

    @SerializedName("Count")
    private int count;

    @SerializedName("List")
    private List<Contact> list;
}
