package pers.hll.aigc4chat.common.protocol.wechat.protocol.request;

import lombok.EqualsAndHashCode;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.constant.DefaultConfig;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.constant.WXHeaderKey;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.constant.WXQueryKey;

import java.util.Map;

/**
 * 获取图片请求
 *
 * @author hll
 * @since 2023/03/11
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
