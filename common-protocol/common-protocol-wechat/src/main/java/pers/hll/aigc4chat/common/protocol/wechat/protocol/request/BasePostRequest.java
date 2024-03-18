package pers.hll.aigc4chat.common.protocol.wechat.protocol.request;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.request.body.BaseRequestBody;


/**
 *
 *
 * @author hll
 * @since 2024/03/14
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public class BasePostRequest<RequestType, ResponseType> extends BaseRequest<RequestType, ResponseType> {

    protected BaseRequestBody baseRequestBody;

    public BasePostRequest(String uri) {
        super(uri);
    }

    public String buildRequestBody() {
        return null;
    }

    public BasePostRequest<RequestType, ResponseType> setBaseRequestBody(BaseRequestBody baseRequestBody) {
        this.baseRequestBody = baseRequestBody;
        return this;
    }
}
