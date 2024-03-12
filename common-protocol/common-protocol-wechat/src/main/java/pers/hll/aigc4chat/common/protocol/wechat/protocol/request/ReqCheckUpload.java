package pers.hll.aigc4chat.common.protocol.wechat.protocol.request;

import me.xuxiaoxiao.xtools.common.XTools;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.request.BaseRequest;

import java.io.File;

/**
 * 检查上传
 *
 * @author hll
 * @author 2024/03/10
 */
public class ReqCheckUpload {

    public final pers.hll.aigc4chat.common.protocol.wechat.protocol.request.BaseRequest BaseRequest;

    public final String FileMd5;

    public final String FileName;

    public final long FileSize;

    public final int FileType;

    public String FromUserName;

    public String ToUserName;

    public ReqCheckUpload(BaseRequest baseRequest, File file, String fromUserName, String toUserName) {
        this.BaseRequest = baseRequest;
        this.FileMd5 = XTools.md5(file);
        this.FileName = file.getName();
        this.FileSize = file.length();
        this.FileType = 7;
        this.FromUserName = fromUserName;
        this.ToUserName = toUserName;
    }
}
