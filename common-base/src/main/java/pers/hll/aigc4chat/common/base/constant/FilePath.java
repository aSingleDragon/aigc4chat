package pers.hll.aigc4chat.common.base.constant;

import java.io.File;

/**
 *
 * @author hll
 * @since 2024/03/10
 */
public interface FilePath {

    String BASE_PATH = "./file";

    String SEP = File.separator;

    String LOGIN_QR_CODE = BASE_PATH + SEP + "qrcode" + SEP + "login.png";

    String IMAGE = BASE_PATH + SEP + "image" + SEP;

    String VOICE = BASE_PATH + SEP + "voice" + SEP;

    String VIDEO = BASE_PATH + SEP + "video" + SEP;

    String MEDIA = BASE_PATH + SEP + "video" + SEP;

    String ME = BASE_PATH + SEP + "me" + SEP;

    String DB = BASE_PATH + SEP + "db" + SEP;
}
