package pers.hll.aigc4chat.common.protocol.wechat.protocol.request;

import lombok.EqualsAndHashCode;
import pers.hll.aigc4chat.common.base.constant.ContentType;
import pers.hll.aigc4chat.common.base.util.BaseUtil;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.constant.*;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.request.body.WebWxUploadMediaReqBody;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.request.form.FormFile;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.response.WebWxUploadMediaResp;

import java.io.File;
import java.util.Map;

/**
 * 上传媒体文件请求
 *
 * @author hll
 * @since 2024/03/11
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

    private Long chunk;

    private Long chunks;

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

    public WebWxUploadMediaReq setChunk(Long chunk) {
        this.chunk = chunk;
        return this;
    }

    public WebWxUploadMediaReq setChunks(Long chunks) {
        this.chunks = chunks;
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

        Map<String, String> formText = getFormText();
        formText.put(WXFormKey.ID, id);
        formText.put(WXFormKey.NAME, name);
        formText.put(WXFormKey.TYPE, type);
        formText.put(WXFormKey.LAST_MODIFIED_DATE, lastModifiedDate);
        formText.put(WXFormKey.SIZE, String.valueOf(size));
        if (chunks != null) {
            formText.put(WXFormKey.CHUNKS, String.valueOf(chunks));
        }
        if (chunk != null) {
            formText.put(WXFormKey.CHUNK, String.valueOf(chunk));
        }
        formText.put(WXFormKey.MEDIA_TYPE, mediaType);
        formText.put(WXFormKey.UPLOAD_MEDIA_REQUEST, uploadMediaRequest);
        formText.put(WXFormKey.WEB_WX_DATA_TICKET, webWxDataTicket);
        formText.put(WXFormKey.PASS_TICKET, passTicket);

        return this;
    }

    @Override
    public WebWxUploadMediaResp convertRespBodyToObj(String strEntity) {
        return BaseUtil.GSON.fromJson(strEntity, WebWxUploadMediaResp.class);
    }

    public WebWxUploadMediaReq setFormFile(FormFile formFile) {
        this.formFile = formFile;
        return this;
    }
}
