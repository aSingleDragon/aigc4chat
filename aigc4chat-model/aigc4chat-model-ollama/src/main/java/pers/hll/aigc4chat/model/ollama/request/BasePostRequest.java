package pers.hll.aigc4chat.model.ollama.request;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import pers.hll.aigc4chat.base.exception.BizException;

/**
 *
 * @author hll
 * @since 2024/04/30
 */
@Slf4j
@Getter
@Setter
@Accessors(chain = true)
public abstract class BasePostRequest<Q, R> extends BaseRequest<R> {

    private Q requestBody;

    protected BasePostRequest(String uri) {
        super(uri);
    }

    /**
     * 构造请求 body
     * <p>有的 post 请求, 没有请求体, 未了防止不必要的异常抛出, 这里默认返回 null
     *
     * @return 请求的 JSON 的 body
     */
    public String buildRequestBody() {
        if (requestBody == null) {
            throw BizException.of("{} request body is null", getUri());
        }
        return GSON.toJson(requestBody);
    }
}
