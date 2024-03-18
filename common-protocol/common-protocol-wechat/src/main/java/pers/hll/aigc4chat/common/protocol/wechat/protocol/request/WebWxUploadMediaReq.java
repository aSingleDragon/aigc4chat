package pers.hll.aigc4chat.common.protocol.wechat.protocol.request;

import lombok.EqualsAndHashCode;
import pers.hll.aigc4chat.common.base.constant.ContentType;
import pers.hll.aigc4chat.common.base.util.BaseUtil;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.constant.DefaultConfig;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.constant.WXHeaderKey;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.constant.WXQueryKey;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.constant.WXQueryValue;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.request.body.BaseRequestBody;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.request.body.Msg;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.request.body.WebWxSendMsgReqBody;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.request.body.WebWxUploadMediaReqBody;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.response.WebWxSendMsgResp;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.response.WebWxUploadMediaResp;

import java.io.File;
import java.util.Map;

/**
 *
 * @author hll
 * @since 2023/03/11
 */
@EqualsAndHashCode(callSuper = true)
public class WebWxUploadMediaReq extends BasePostRequest<WebWxUploadMediaReq, WebWxUploadMediaResp> {

    private String id;

    private String name;

    private String type;

    private String lastModifiedDate;

    private long size;

    private String mediaType;

    private String uploadMediaRequest;

    private String webWxDataTicket;

    private String passTicket;

    private File file;

    public WebWxUploadMediaReq(String uri) {
        super(uri);
    }

    public WebWxUploadMediaReq setId(String id) {
        this.id = id;
        return this;
    }

    public WebWxUploadMediaReq setName(String name) {
        this.name = name;
        return this;
    }

    public WebWxUploadMediaReq setType(String type) {
        this.type = type;
        return this;
    }

    public WebWxUploadMediaReq setLastModifiedDate(String lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }

    public WebWxUploadMediaReq setSize(long size) {
        this.size = size;
        return this;
    }

    public WebWxUploadMediaReq setMediaType(String mediaType) {
        this.mediaType = mediaType;
        return this;
    }

    public WebWxUploadMediaReq setUploadMediaRequest(String uploadMediaRequest) {
        this.uploadMediaRequest = uploadMediaRequest;
        return this;
    }

    public WebWxUploadMediaReq setWebWxDataTicket(String webWxDataTicket) {
        this.webWxDataTicket = webWxDataTicket;
        return this;
    }

    public WebWxUploadMediaReq setFile(File file) {
        this.file = file;
        return this;
    }

    public WebWxUploadMediaReq setPassTicket(String passTicket) {
        this.passTicket = passTicket;
        return this;
    }

    @Override
    public WebWxUploadMediaReq build() {

        Map<String, Object> requestParamMap = getRequestParamMap();
        requestParamMap.put(WXQueryKey.F, WXQueryValue.JSON);

        Map<String, String> headerMap = getHeaderMap();
        headerMap.put(WXHeaderKey.USER_AGENT, DefaultConfig.USER_AGENT);
        headerMap.put(WXHeaderKey.CONTENT_TYPE, ContentType.JSON);
        return this;
    }

    @Override
    public String buildRequestBody() {
        return BaseUtil.GSON.toJson(new WebWxUploadMediaReqBody()
                .setId(id)
                .setName(name)
                .setType(type)
                .setLastModifiedDate(lastModifiedDate)
                .setSize(size)
                .setMediaType(mediaType)
                .setUploadMediaRequest(uploadMediaRequest)
                .setWebWxDataTicket(webWxDataTicket)
                .setPassTicket(passTicket)
                .setFile(file));
    }

    @Override
    public WebWxUploadMediaResp convertRespBodyToObj(String strEntity) {
        return BaseUtil.GSON.fromJson(strEntity, WebWxUploadMediaResp.class);
    }
}
