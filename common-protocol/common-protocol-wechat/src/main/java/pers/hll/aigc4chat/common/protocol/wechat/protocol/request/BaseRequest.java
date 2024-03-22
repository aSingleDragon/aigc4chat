package pers.hll.aigc4chat.common.protocol.wechat.protocol.request;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import pers.hll.aigc4chat.common.base.Builder;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 基础请求
 *
 * @author hll
 * @author 2024/03/10
 */
@Data
@Slf4j
@Accessors(chain = true)
public class BaseRequest<RequestType, ResponseType> implements Builder<RequestType> {

    /**
     * 请求资源地址
     */
    private final String uri;

    /**
     * 是否允许跳转 默认允许 微信只有一个接口不允许跳转
     */
    protected boolean redirectsEnabled = true;

    /**
     * 是否有文件流 默认没有
     */
    protected boolean fileStreamAvailable = false;

    /**
     * 文件流 默认为空 只有有文件流时才需要
     */
    protected InputStream inputStream = null;

    /**
     * 请求参数 map
     */
    private final Map<String, Object> requestParamMap = new HashMap<>();

    /**
     * 请求头 map
     */
    private final Map<String, String> headerMap = new HashMap<>();

    /**
     * 路径变量 map
     */
    private final Map<String, String> pathVariableMap = new HashMap<>();

    public BaseRequest(String uri) {
        this.uri = uri;
    }

    /**
     * 响应体转换成响应对象
     * <p> 因为微信的响应消息类型不是统一的 有JSON, XML, 及其它类型
     * <p> 所以需要子类重写此方法 给出具体实现
     *
     * @param stringEntity 响应体
     * @return 对象 (范型支持类)
     */
    public ResponseType convertRespBodyToObj(String stringEntity) {
        log.warn("响应数据没有转换实现:{}", stringEntity);
        return null;
    }

    /**
     * 构建请求
     *
     * @return 请求对象 (范型支持类)
     */
    @Override
    public RequestType build() {
        log.error("请求没有构造实现!");
        throw new UnsupportedOperationException("此方法需要子类重写!");
    }
}
