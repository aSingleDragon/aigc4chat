package pers.hll.aigc4chat.common.base.constant;

import java.io.File;

/**
 * @author hll
 * @since 2024/03/10
 */
public interface FilePath {

    String SEP = File.separator;

    String BASE_PATH = "./file";

    String CHAT_HISTORY = BASE_PATH + SEP + "chat-history";

    String UPLOAD = BASE_PATH + SEP + "upload";

    String WECHAT_HISTORY = CHAT_HISTORY + SEP + "wechat";

    String WECHAT_LOGIN_QR_CODE = WECHAT_HISTORY + SEP + "qrcode" + SEP + "login.png";

    String WECHAT_IMAGE = WECHAT_HISTORY  + SEP + "image" + SEP;

    String WECHAT_VOICE = WECHAT_HISTORY + SEP + "voice" + SEP;

    String WECHAT_VIDEO = WECHAT_HISTORY + SEP + "video" + SEP;

    String WECHAT_MEDIA = WECHAT_HISTORY  + SEP + "video" + SEP;

    String WECHAT_ME = WECHAT_HISTORY + SEP + "me" + SEP + "avatar.jpg";

    String DB = BASE_PATH + SEP + "db" + SEP;
}
