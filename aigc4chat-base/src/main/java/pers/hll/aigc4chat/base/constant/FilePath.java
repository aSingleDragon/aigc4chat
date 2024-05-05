package pers.hll.aigc4chat.base.constant;

import java.io.File;

/**
 * 文件路径常量类 各种类型的文件的存储路径
 *
 * @author hll
 * @since 2024/03/10
 */
public interface FilePath {

    String SEP = File.separator;

    /**
     * 项目根目录
     */
    String BASE_PATH = System.getProperty("user.dir") + SEP + "file";

    /**
     * 聊天记录
     */
    String CHAT_HISTORY = BASE_PATH + SEP + "chat-history";

    /**
     * 项目文件-不能随意删除
     */
    String PROJECT = BASE_PATH + SEP + "project";

    /**
     * XML 配置文件目录
     */
    String XML_CONFIG = PROJECT + SEP + "config" + SEP;

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
