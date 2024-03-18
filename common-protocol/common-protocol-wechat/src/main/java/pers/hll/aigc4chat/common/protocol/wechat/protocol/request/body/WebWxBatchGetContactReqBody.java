package pers.hll.aigc4chat.common.protocol.wechat.protocol.request.body;


import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class WebWxBatchGetContactReqBody extends BasePostRequestBaseBody {

    @SerializedName("Count")
    private int count;

    @SerializedName("List")
    private List<Contact> list;
}
