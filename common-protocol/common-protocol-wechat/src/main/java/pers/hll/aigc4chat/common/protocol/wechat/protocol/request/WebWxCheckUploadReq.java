package pers.hll.aigc4chat.common.protocol.wechat.protocol.request;

import lombok.EqualsAndHashCode;
import me.xuxiaoxiao.xtools.common.XTools;
import pers.hll.aigc4chat.common.base.constant.ContentType;
import pers.hll.aigc4chat.common.base.util.BaseUtil;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.constant.DefaultConfig;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.constant.WXHeaderKey;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.request.body.BaseRequestBody;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.request.body.WebWxSendMsgReqBody;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.response.WebWxCheckUploadResp;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.response.WebWxSendMsgResp;

import java.io.File;
import java.util.Map;

/**
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
                        .setFileMd5(BaseUtil.md5("MD5", file))
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
