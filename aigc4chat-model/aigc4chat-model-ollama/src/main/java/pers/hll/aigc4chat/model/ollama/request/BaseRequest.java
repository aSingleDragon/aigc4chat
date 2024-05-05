package pers.hll.aigc4chat.model.ollama.request;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.http.HttpEntity;
import pers.hll.aigc4chat.base.util.XmlUtil;
import pers.hll.aigc4chat.base.xml.OllamaConfig;
import pers.hll.aigc4chat.model.ollama.constant.Role;
import pers.hll.aigc4chat.model.ollama.gson.RoleSerializer;
import pers.hll.aigc4chat.model.ollama.gson.LocalDateTimeDeserializer;

import java.time.LocalDateTime;

/**
 *
 * @author hll
 * @since 2024/04/30
 */
@Data
@Slf4j
@Accessors(chain = true)
public abstract class BaseRequest<R> {

    private static final OllamaConfig OLLAMA_CONFIG;

    protected static final Gson GSON;

    private R responseBody;

    /**
     * 请求资源地址
     */
    private final String uri;

    static {
        OllamaConfig ollamaConfig = XmlUtil.readXmlConfig(OllamaConfig.class);
        if (ollamaConfig == null ||
                ObjectUtils.anyNull(ollamaConfig.getHost(), ollamaConfig.getPort(), ollamaConfig.getProtocol())) {
            OLLAMA_CONFIG = new OllamaConfig("http", "localhost", 11434);
        } else {
            OLLAMA_CONFIG = ollamaConfig;
        }
        GSON = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer())
                .registerTypeAdapter(Role.class, new RoleSerializer())
                .create();
    }

    protected BaseRequest(String endpoint) {
        this.uri = OLLAMA_CONFIG.getUri() + endpoint;
    }

    /**
     * 响应体转换成响应对象
     *
     * @param httpEntity 响应体
     * @return 对象 (范型支持类)
     */
    public abstract R convertHttpEntityToObj(HttpEntity httpEntity);
}
