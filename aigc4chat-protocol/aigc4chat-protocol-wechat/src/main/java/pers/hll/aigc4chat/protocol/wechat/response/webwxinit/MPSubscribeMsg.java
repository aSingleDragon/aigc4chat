package pers.hll.aigc4chat.protocol.wechat.response.webwxinit;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

/**
 * 微信公众号订阅消息
 *
 * @author hll
 * @since 2024/03/19
 */
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