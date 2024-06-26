package pers.hll.aigc4chat.protocol.wechat.request;

import lombok.EqualsAndHashCode;
import pers.hll.aigc4chat.base.constant.ContentType;
import pers.hll.aigc4chat.base.util.BaseUtil;
import pers.hll.aigc4chat.protocol.wechat.constant.DefaultConfig;
import pers.hll.aigc4chat.protocol.wechat.constant.WXHeaderKey;
import pers.hll.aigc4chat.protocol.wechat.request.body.BaseRequestBody;
import pers.hll.aigc4chat.protocol.wechat.request.body.WebWxCheckUploadReqBody;
import pers.hll.aigc4chat.protocol.wechat.response.WebWxCheckUploadResp;

import java.io.File;
import java.util.Map;

/**
 * 检查文件是否已经在微信服务器请求
 *
 * @author hll
 * @since 2024/03/11
 */
@EqualsAndHashCode(callSuper = true)
public class WebWxCheckUploadReq extends BasePostRequest<WebWxCheckUploadReq, WebWxCheckUploadResp> {

    private String filePath;

    private String fromUserName;

    private String toUserName;

    public WebWxCheckUploadReq(String uri) {
        super(uri);
    }

    public WebWxCheckUploadReq setFilePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    public WebWxCheckUploadReq setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
        return this;
    }

    public WebWxCheckUploadReq setToUserName(String toUserName) {
        this.toUserName = toUserName;
        return this;
    }

    @Override
    public WebWxCheckUploadReq build() {
        Map<String, String> headerMap = getHeaderMap();
        headerMap.put(WXHeaderKey.USER_AGENT, DefaultConfig.USER_AGENT);
        headerMap.put(WXHeaderKey.CONTENT_TYPE, ContentType.JSON);
        return this;
    }

    @Override
    public String buildRequestBody() {
        File file = new File(filePath);
        return BaseUtil.GSON.toJson(new WebWxCheckUploadReqBody()
                        .setFileMd5(BaseUtil.md5(file))
                        .setFileName(file.getName())
                        .setFileSize(file.length())
                        .setFileType(7)
                        .setFromUserName(fromUserName)
                        .setToUserName(toUserName)
                        .setBaseRequestBody(this.getBaseRequestBody()));
    }

    @Override
    public WebWxCheckUploadResp convertRespBodyToObj(String strEntity) {
        return BaseUtil.GSON.fromJson(strEntity, WebWxCheckUploadResp.class);
    }

    @Override
    public WebWxCheckUploadReq setBaseRequestBody(BaseRequestBody baseRequestBody) {
        this.baseRequestBody = baseRequestBody;
        return this;
    }
}
