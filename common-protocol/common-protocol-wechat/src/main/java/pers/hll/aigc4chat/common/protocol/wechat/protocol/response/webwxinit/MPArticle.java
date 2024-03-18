package pers.hll.aigc4chat.common.protocol.wechat.protocol.response.webwxinit;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
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