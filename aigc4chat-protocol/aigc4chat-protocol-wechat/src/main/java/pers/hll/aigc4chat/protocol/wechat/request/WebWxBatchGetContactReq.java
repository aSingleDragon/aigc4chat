package pers.hll.aigc4chat.protocol.wechat.request;

import lombok.EqualsAndHashCode;
import pers.hll.aigc4chat.base.constant.ContentType;
import pers.hll.aigc4chat.base.util.BaseUtil;
import pers.hll.aigc4chat.protocol.wechat.constant.DefaultConfig;
import pers.hll.aigc4chat.protocol.wechat.constant.WXHeaderKey;
import pers.hll.aigc4chat.protocol.wechat.constant.WXQueryKey;
import pers.hll.aigc4chat.protocol.wechat.constant.WXQueryValue;
import pers.hll.aigc4chat.protocol.wechat.request.body.BaseRequestBody;
import pers.hll.aigc4chat.protocol.wechat.request.body.Contact;
import pers.hll.aigc4chat.protocol.wechat.request.body.WebWxBatchGetContactReqBody;
import pers.hll.aigc4chat.protocol.wechat.response.WebWxBatchGetContactResp;

import java.util.List;
import java.util.Map;

/**
 * 批量获取联系人请求
 *
 * @author hll
 * @since 2024/03/11
 */
@EqualsAndHashCode(callSuper = true)
public class WebWxBatchGetContactReq extends BasePostRequest<WebWxBatchGetContactReq, WebWxBatchGetContactResp> {

    private String passTicket;

    private int count;

    private List<Contact> list;

    public WebWxBatchGetContactReq(String uri) {
        super(uri);
    }

    public WebWxBatchGetContactReq setCount(int count) {
        this.count = count;
        return this;
    }

    public WebWxBatchGetContactReq setList(List<Contact> list) {
        this.list = list;
        return this;
    }

    @Override
    public WebWxBatchGetContactReq build() {

        Map<String, Object> requestParamMap = getRequestParamMap();
        requestParamMap.put(WXQueryKey.TYPE, WXQueryValue.EX);
        requestParamMap.put(WXQueryKey.R, BaseUtil.getEpochSecond());

        Map<String, String> headerMap = getHeaderMap();
        headerMap.put(WXHeaderKey.CONTENT_TYPE, ContentType.JSON);
        headerMap.put(WXHeaderKey.USER_AGENT, DefaultConfig.USER_AGENT);
        return this;
    }

    @Override
    public String buildRequestBody() {
        return BaseUtil.GSON.toJson(new WebWxBatchGetContactReqBody()
                .setCount(count)
                .setList(list)
                .setBaseRequestBody(this.getBaseRequestBody()));
    }

    @Override
    public WebWxBatchGetContactResp convertRespBodyToObj(String strEntity) {
        return BaseUtil.GSON.fromJson(strEntity, WebWxBatchGetContactResp.class);
    }

    public WebWxBatchGetContactReq setPassTicket(String passTicket) {
        this.passTicket = passTicket;
        return this;
    }

    @Override
    public WebWxBatchGetContactReq setBaseRequestBody(BaseRequestBody baseRequestBody) {
        this.baseRequestBody = baseRequestBody;
        return this;
    }
}
