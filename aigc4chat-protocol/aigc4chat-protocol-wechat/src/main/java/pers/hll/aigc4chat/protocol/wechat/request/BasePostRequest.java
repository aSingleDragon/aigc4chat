package pers.hll.aigc4chat.protocol.wechat.request;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import pers.hll.aigc4chat.protocol.wechat.request.body.BaseRequestBody;
import pers.hll.aigc4chat.protocol.wechat.request.form.FormFile;

import java.util.LinkedHashMap;
import java.util.Map;


/**
 * 基础 post 请求
 *
 * @author hll
 * @since 2024/03/14
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public class BasePostRequest<RequestType, ResponseType> extends BaseRequest<RequestType, ResponseType> {

    /**
     * 微信响应体的共有部分
     */
    protected BaseRequestBody baseRequestBody;

    /**
     * form 表单 String
     */
    protected Map<String, String> formText = new LinkedHashMap<>();

    /**
     * from 表单 文件
     */
    protected FormFile formFile;

    public BasePostRequest(String uri) {
        super(uri);
    }

    /**
     * 构造请求 body
     * <p>有的 post 请求, 没有请求体, 未了防止不必要的异常抛出, 这里默认返回 null
     *
     * @return 请求的 JSON 的 body
     */
    public String buildRequestBody() {
        return null;
    }

    /**
     * 设置请求 body 的共有部分, 链式调用
     *
     * @param baseRequestBody 请求 body 的共有部分
     * @return this
     */
    public BasePostRequest<RequestType, ResponseType> setBaseRequestBody(BaseRequestBody baseRequestBody) {
        this.baseRequestBody = baseRequestBody;
        return this;
    }
}
