package pers.hll.aigc4chat.base.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import jakarta.servlet.http.HttpServletResponse;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.EnumMap;
import java.util.Map;

/**
 * 二维码工具类
 *
 * @author hll
 * @since 2024/03/10
 */
@Slf4j
@UtilityClass
public class QRCodeUtil {

    /**
     * CODE_WIDTH：二维码宽度，单位像素
     */
    private static final int CODE_WIDTH = 400;

    /**
     * CODE_HEIGHT：二维码高度，单位像素
     */
    private static final int CODE_HEIGHT = 400;

    /**
     * FRONT_COLOR：二维码前景色，0x000000 表示黑色
     */
    private static final int FRONT_COLOR = 0x000000;

    /**
     * BACKGROUND_COLOR：二维码背景色，0xFFFFFF 表示白色
     * 演示用 16 进制表示，和前端页面 CSS 的取色是一样的，注意前后景颜色应该对比明显，如常见的黑白
     */
    private static final int BACKGROUND_COLOR = 0xFFFFFF;

    /**
     * 获取默认配置
     *
     * @return 默认配置
     */
    private Map<EncodeHintType, Serializable> getDefaultConfig() {
        Map<EncodeHintType, Serializable> hints = new EnumMap<>(EncodeHintType.class);
        // 字符集
        hints.put(EncodeHintType.CHARACTER_SET, StandardCharsets.UTF_8.name());
        // ErrorCorrectionLevel：误差校正等级
        // L = ~7% correction、M = ~15% correction、Q = ~25% correction、H = ~30% correction
        // 不设置时，默认为 L 等级，等级不一样，生成的图案不同，但扫描的结果是一样的
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        return hints;
    }

    /**
     * 打印二维码到控制台
     * 在 IDEA 中，由于控制台的文字行高比较大，输出的二维码内容宽高不一致，导致无法识别。
     * 可以将其保存至文件，或者用 终端/命令行 打印出来。
     *
     * @param filePath 保存路径
     * @param content 二维码内容
     */
    public void writeInText(String filePath, String content) {
        // 二维码宽度
        int width = 1;
        // 二维码高度
        int height = 1;
        StringBuilder qrCodeStringBuilder = new StringBuilder();
        // 定义二维码的参数
        Map<EncodeHintType, Serializable> hints = getDefaultConfig();
        // 打印二维码
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            for (int j = 0; j < bitMatrix.getHeight(); j++) {
                for (int i = 0; i < bitMatrix.getWidth(); i++) {
                    if (bitMatrix.get(i, j)) {
                        qrCodeStringBuilder.append("■");
                    } else {
                        qrCodeStringBuilder.append(" ");
                    }
                }
                qrCodeStringBuilder.append(System.lineSeparator());
                FileUtils.write(new File(filePath), qrCodeStringBuilder.toString(), StandardCharsets.UTF_8);
            }
        } catch (WriterException | IOException e) {
            log.error("二维码[{}]打印失败:", content, e);
        }
    }

    /**
     * 生成二维码
     *
     * @param path 二维码图片保存路径
     * @param content 二维码内容
     */
    public void writeInImage(String path, String content) {
        Path parentDir = Paths.get(path).getParent();
        if (!Files.exists(parentDir)) {
            try {
                Files.createDirectories(parentDir);
            } catch (IOException e) {
                log.error("创建目录失败: {}", parentDir.toAbsolutePath(), e);
            }
        }
        try {
            ImageIO.write(getBufferedImage(content), "png", new File(path));
        } catch (IOException e) {
            log.error("二维码图片写入异常:", e);
        }
    }

    /**
     * 将二维码写入到响应的输出流中
     *
     * @param content 二维码内容
     */
    public void write2Response(String content, HttpServletResponse response) {
        try {
            response.setContentType("image/png");
            ImageIO.write(getBufferedImage(content), "png", response.getOutputStream());
        } catch (IOException e) {
            log.error("二维码图片写入异常:", e);
        }
    }

    private BufferedImage getBufferedImage(String content) {
        // com.google.zxing.EncodeHintType：编码提示类型,枚举类型
        Map<EncodeHintType, Serializable> hints = getDefaultConfig();

        // EncodeHintType.MARGIN：设置二维码边距，单位像素，值越小，二维码距离四周越近
        hints.put(EncodeHintType.MARGIN, 1);

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        BitMatrix bitMatrix = null;
        try {
            bitMatrix = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, CODE_WIDTH, CODE_HEIGHT, hints);
        } catch (WriterException e) {
            log.error("二维码图片生成异常:", e);
        }
        BufferedImage bufferedImage = new BufferedImage(CODE_WIDTH, CODE_HEIGHT, BufferedImage.TYPE_INT_BGR);
        for (int x = 0; x < CODE_WIDTH; x++) {
            for (int y = 0; y < CODE_HEIGHT; y++) {
                bufferedImage.setRGB(x, y, bitMatrix.get(x, y) ? FRONT_COLOR : BACKGROUND_COLOR);
            }
        }
        return bufferedImage;
    }

    /**
     * 将由content生成的二维码，利用系统的接口打开并展示该二维码
     * <p> 如果在Spring的服务里调用这个方法回报错
     *
     * @param path 二维码图片保存路径
     * @param content 要生成二维码的内容
     */
    public void writeInImageAndOpen(String path, String content) {
        writeInImage(path, content);
        try {
            Desktop.getDesktop().open(new File(path));
        } catch (IOException e) {
            log.error("打开二维码图片失败:", e);
        }
    }
}
