package pers.hll.aigc4chat.common.protocol.wechat.protocol.response.webwxinit;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class MPSubscribeMsg {

    @SerializedName("UserName")
    private String userName;

    @SerializedName("NickName")
    private String nickName;

    @SerializedName("Time")
    private long time;

    @SerializedName("MPArticleCount")
    private int mPArticleCount;

    @SerializedName("MPArticleList")
    private List<MPArticle> mPArticleList;
}