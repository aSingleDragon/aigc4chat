package pers.hll.aigc4chat.common.protocol.wechat.protocol.request;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import pers.hll.aigc4chat.common.base.Builder;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hll
 * @author 2024/03/10
 */
@Data
@Slf4j
@Accessors(chain = true)
public class BaseRequest<RequestType, ResponseType> implements Builder<RequestType> {

    private final String uri;

    protected boolean redirectsEnabled = true;

    protected boolean fileStreamAvailable = false;

    protected InputStream inputStream = null;

    private final Map<String, Object> requestParamMap = new HashMap<>();

    private final Map<String, String> headerMap = new HashMap<>();

    private final Map<String, String> pathVariableMap = new HashMap<>();

    public BaseRequest(String uri) {
        this.uri = uri;
    }

    public ResponseType convertRespBodyToObj(String stringEntity) {
        log.warn("响应数据没有转换实现:{}", stringEntity);
        return null;
        //throw new UnsupportedOperationException("此方法需要子类重写!");
    }

    @Override
    public RequestType build() {
        log.error("请求没有构造实现!");
        throw new UnsupportedOperationException("此方法需要子类重写!");
    }
}
