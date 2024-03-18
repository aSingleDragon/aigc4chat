package pers.hll.aigc4chat.common.protocol.wechat.protocol.request.body;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author hll
 * @author 2024/03/10
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class WebWxVerifyUserReqBody extends BasePostRequestBaseBody {

    @SerializedName("Opcode")
    private int opCode;

    @SerializedName("VerifyUserListSize")
    private int verifyUserListSize;

    @SerializedName("VerifyUserList")
    private List<VerifyUser> verifyUserList;

    @SerializedName("VerifyContent")
    private String verifyContent;

    @SerializedName("SceneListCount")
    private int sceneListCount;

    @SerializedName("SceneList")
    private List<Integer> sceneList;

    @SerializedName("skey")
    private String skey;
}
