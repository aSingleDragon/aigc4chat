package pers.hll.aigc4chat.protocol.wechat.request;

import lombok.EqualsAndHashCode;

/**
 * 获取图片请求
 *
 * @author hll
 * @since 2024/03/11
 */
@EqualsAndHashCode(callSuper = true)
public class WebWxGetAvatarReq extends BaseRequest<WebWxGetAvatarReq, Object> {


    public WebWxGetAvatarReq(String uri) {
        super(uri);
    }

    @Override
    public WebWxGetAvatarReq build() {
        return this;
    }
}
