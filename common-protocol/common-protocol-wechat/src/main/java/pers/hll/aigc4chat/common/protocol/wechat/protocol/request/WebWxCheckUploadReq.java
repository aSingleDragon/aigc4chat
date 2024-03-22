package pers.hll.aigc4chat.common.protocol.wechat.protocol.request;

import lombok.EqualsAndHashCode;
import pers.hll.aigc4chat.common.base.constant.ContentType;
import pers.hll.aigc4chat.common.base.util.BaseUtil;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.constant.DefaultConfig;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.constant.WXHeaderKey;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.request.body.BaseRequestBody;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.request.body.WebWxCheckUploadReqBody;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.response.WebWxCheckUploadResp;

import java.io.File;
import java.util.Map;

/**
 * 检查文件是否已经在微信服务器请求
 *
 * @author hll
 * @since 2023/03/11
 */
@EqualsAndHashCode(callSuper = true)
public class WebWxCheckUploadReq extends BasePostRequest<WebWxCheckUploadReq, WebWxCheckUploadResp> {

    private File file;

    private String fromUserName;

    private String toUserName;

    public WebWxCheckUploadReq(String uri) {
        super(uri);
    }

    public WebWxCheckUploadReq setFile(File file) {
        this.file = file;
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
