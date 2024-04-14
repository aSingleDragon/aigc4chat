package pers.hll.aigc4chat.common.base.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import java.io.*;
import java.util.Iterator;

/**
 * 常用文件的文件头如下：(以前六位为准)
 * JPEG (jpg)，文件头：FFD8FF
 * PNG (png)，文件头：89504E47
 * GIF (gif)，文件头：47494638
 * TIFF (tif)，文件头：49492A00
 * Windows Bitmap (bmp)，文件头：424D
 * CAD (dwg)，文件头：41433130
 * Adobe Photoshop (psd)，文件头：38425053
 * Rich Text Format (rtf)，文件头：7B5C727466
 * XML (xml)，文件头：3C3F786D6C
 * HTML (html)，文件头：68746D6C3E
 * Email [thorough only] (eml)，文件头：44656C69766572792D646174653A
 * Outlook Express (dbx)，文件头：CFAD12FEC5FD746F
 * Outlook (pst)，文件头：2142444E
 * MS Word/Excel (xls.or.doc)，文件头：D0CF11E0
 * MS Access (mdb)，文件头：5374616E64617264204A
 * WordPerfect (wpd)，文件头：FF575043
 * Postscript (eps.or.ps)，文件头：252150532D41646F6265
 * Adobe Acrobat (pdf)，文件头：255044462D312E
 * Quicken (qdf)，文件头：AC9EBD8F
 * Windows Password (pwl)，文件头：E3828596
 * ZIP Archive (zip)，文件头：504B0304
 * RAR Archive (rar)，文件头：52617221
 * Wave (wav)，文件头：57415645
 * AVI (avi)，文件头：41564920
 * Real Audio (ram)，文件头：2E7261FD
 * Real Media (rm)，文件头：2E524D46
 * MPEG (mpg)，文件头：000001BA
 * MPEG (mpg)，文件头：000001B3
 * Quicktime (mov)，文件头：6D6F6F76
 * Windows Media (asf)，文件头：3026B2758E66CF11
 * MIDI (mid)，文件头：4D546864
 *
 * @author <a href="https://www.cnblogs.com/ming-blogs/p/15261473.html">明天,你好啊 </a>
 * @since 2024/03/14
 */
@Slf4j
@UtilityClass
public class ImageTypeUtils {

    public final String JPG = ".jpg";

    public final String GIF = ".gif";

    public final String PNG = ".png";

    public final String BMP = ".bmp";

    public final String WEBP = ".webp";

    public final String TIF = ".tif";

    public final String UNKNOWN = ".unknown";

    /**
     * byte数组转换成16进制字符串
     *
     * @param headerBytes byte数组
     * @return 文件流开始的字节的16进制表示
     */
    private String bytesToHexString(byte[] headerBytes) {
        StringBuilder stringBuilder = new StringBuilder();
        if (headerBytes == null || headerBytes.length == 0) {
            return "";
        }
        for (byte b : headerBytes) {
            int v = b & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    public String typeOf(String path) throws IOException {
        return typeOf(new File(path));
    }

    public String typeOf(File file) throws IOException {
        return typeOf(new FileInputStream(file));
    }

    /**
     * 根据文件流判断图片类型
     *
     * @param fis 文件流
     * @return 图片类型
     */
    public String typeOf(InputStream fis) throws IOException {
        byte[] headerBytes = new byte[4];
        IOUtils.readFully(fis, headerBytes);
        String headerHex = bytesToHexString(headerBytes).toUpperCase();
        return switch (headerHex) {
            case "FFD8FF" -> JPG;
            case "89504E47" -> PNG;
            case "47494638" -> GIF;
            case "424D" -> BMP;
            case "52494646" -> WEBP;
            case "49492A00" -> TIF;
            default -> getFormatName(headerBytes);
        };
    }

    private String getFormatName(byte[] headerBytes) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(headerBytes);
        Iterator<ImageReader> readers = ImageIO.getImageReaders(bis);
        if (readers.hasNext()) {
            ImageReader reader = readers.next();
            return reader.getFormatName().toUpperCase();
        }
        return UNKNOWN;
    }
}