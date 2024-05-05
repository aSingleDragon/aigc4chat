package pers.hll.aigc4chat.base.util;


import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import pers.hll.aigc4chat.base.exception.BizException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * 判断图片类型工具类
 *
 * @author hll
 * @since 2024/03/27
 */
@Slf4j
@UtilityClass
public class ImgTypeUtil {

    private static final char[] HEX = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * 判断文件的media类型，共有三种，pic：图片，video：短视频，doc：其他文件
     *
     * @param file 需要判断的文件
     * @return 文件的media类型
     */
    public static String fileType(File file) {
        return switch (ImgTypeUtil.fileSuffix(file)) {
            case "jpg", "png", "bmp", "jpeg" -> "pic";
            case "mp4" -> "video";
            default -> "doc";
        };
    }

    /**
     * 获取文件的扩展名，图片类型的文件，会根据文件内容自动判断文件扩展名
     *
     * @param file 要获取文件扩展名的文件
     * @return 文件扩展名
     */
    public static String fileSuffix(File file) {
        try (FileInputStream is = new FileInputStream(file)) {
            byte[] b = new byte[3];
            is.read(b, 0, b.length);
            String fileCode = bytesToHex(b);
            switch (fileCode) {
                case "ffd8ff":
                    return "jpg";
                case "89504e":
                    return "png";
                case "474946":
                    return "gif";
                default:
                    if (fileCode.startsWith("424d")) {
                        return "bmp";
                    } else if (file.getName().lastIndexOf('.') > 0) {
                        return file.getName().substring(file.getName().lastIndexOf('.') + 1);
                    } else {
                        return "";
                    }
            }
        } catch (IOException e) {
            log.error("文件读写异常:", e);
            throw BizException.of("文件读写异常: ", e);
        }
    }

    /**
     * 将字节数组转换成16进制字符串
     *
     * @param bytes 要转换的字节数组
     * @return 转换后的字符串，全小写字母
     */
    private static String bytesToHex(byte[] bytes) {
        char[] chars = new char[bytes.length * 2];
        for (int i = 0; i < bytes.length; i++) {
            byte b = bytes[i];
            chars[i << 1] = HEX[b >>> 4 & 0xf];
            chars[(i << 1) + 1] = HEX[b & 0xf];
        }
        return new String(chars);
    }
}
