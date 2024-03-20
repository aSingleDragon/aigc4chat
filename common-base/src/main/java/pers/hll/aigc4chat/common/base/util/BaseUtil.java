package pers.hll.aigc4chat.common.base.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import me.xuxiaoxiao.xtools.common.XTools;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Random;

@Slf4j
@UtilityClass
public class BaseUtil {

    public final Gson GSON = new GsonBuilder().disableHtmlEscaping().create();

    public final Random RANDOM = new Random();

    /**
     * 获得微信请求的r
     *
     * @return r
     */
    public int getR() {
        return (int) (-(Instant.now().getEpochSecond()) / 1579);
    }

    /**
     * 获得当前时间戳 等价于 python 的 time.time()
     *
     * @return 时间戳 这个方法返回的是自1970年1月1日 00:00:00 UTC（Unix纪元）以来的秒数。它是一个长整型（long），表示的时间精度是秒
     */
    public long getEpochSecond() {
        return Instant.now().getEpochSecond();
    }

    /**
     * 获得当前时间戳
     *
     * @return 时间戳 这个方法返回的是自1970年1月1日 00:00:00 UTC（Unix纪元）以来的毫秒数。它是一个长整型（long），表示的时间精度是毫秒
     */
    public long getEpochMilli() {
        return Instant.now().toEpochMilli();
    }

    public int getR(long epochSecond) {
        return (int) (-epochSecond / 1579);
    }

    public String createDeviceId() {
        //StringBuilder sb = new StringBuilder("e");
        //for (int i = 0; i < 15; i++) {
        //    sb.append(RANDOM.nextInt(10));
        //}
        //return sb.toString();
        double randomNumber = RANDOM.nextDouble();
        String formattedNumber = String.format("%.14f", randomNumber).substring(2, 16);
        return  "e" + formattedNumber;
    }

    public static void main(String[] args) {
        System.out.println(createDeviceId());
    }

    public String createMsgId() {
        StringBuilder sbRandom = new StringBuilder().append(getEpochSecond());
        for (int i = 0; i < 4; i++) {
            sbRandom.append(RANDOM.nextInt(10));
        }
        return sbRandom.toString();
    }

    public String md5(File file) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            log.error("未找到MD5算法:", e);
        }
        try (FileInputStream fileInputStream = new FileInputStream(file);
             DigestInputStream digestInputStream = new DigestInputStream(fileInputStream, messageDigest)) {
            byte[] buffer = new byte[131072];
            while (true) {
                if (digestInputStream.read(buffer) <= 0) {
                    break;
                }
            }
            return bytesToHex(digestInputStream.getMessageDigest().digest());
        } catch (FileNotFoundException e) {
            log.error("文件未找到: ", e);
        } catch (IOException e) {
            log.error("IO异常: ", e);
        }
        return null;
    }

    private static final char[] HEX = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

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
