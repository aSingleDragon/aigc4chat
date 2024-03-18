package pers.hll.aigc4chat.common.protocol.wechat.protocol.response;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.response.webwxinit.User;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author hll
 * @author 2024/03/14
 */
@Data
@RequiredArgsConstructor
public class SyncCheckResp {

    @SerializedName("retcode")
    private final Integer retCode;

    private final Integer selector;
}
