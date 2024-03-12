package pers.hll.aigc4chat.common.protocol.wechat.protocol.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author hll
 * @author 2024/03/10
 */
@Getter
public class BaseRequest {

    @JsonProperty("Uin")
    public String uin;

    @JsonProperty("Sid")
    public String sId;

    @JsonProperty("Skey")
    public String sKey;

    @JsonProperty("DeviceID")
    public String deviceID;

    @Getter
    public String uri;

    @Setter
    @Getter
    private boolean redirectsEnabled;

    private static final Random RANDOM = new Random();

    private final Map<String, String> requestParamMap = new HashMap<>();

    private final Map<String, String> headerMap = new HashMap<>();

    private final Map<String, String> pathVariableMap = new HashMap<>();

    public BaseRequest(String uin, String sId, String sKey) {
        this.uin = uin;
        this.sId = sId;
        this.sKey = sKey;
        this.redirectsEnabled = true;
        this.deviceID = createDeviceId();
    }

    public BaseRequest(String uri) {
        this.uri = uri;
    }

    public static String createDeviceId() {
        StringBuilder sb = new StringBuilder("e");
        for (int i = 0; i < 15; i++) {
            sb.append(RANDOM.nextInt(10));
        }
        return sb.toString();
    }

    public <T> T stringToGeneric(String stringEntity) {
        return null;
    }
}
