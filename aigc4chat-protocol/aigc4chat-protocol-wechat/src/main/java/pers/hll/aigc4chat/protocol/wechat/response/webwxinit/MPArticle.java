package pers.hll.aigc4chat.protocol.wechat.response.webwxinit;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * 微信公众号文章
 *
 * @author hll
 * @since 2024/03/19
 */
@Data
public class MPArticle {

    @SerializedName("Title")
    private String title;

    @SerializedName("Digest")
    private String digest;

    @SerializedName("Cover")
    private String cover;

    @SerializedName("Url")
    private String url;
}