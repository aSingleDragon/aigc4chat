package pers.hll.aigc4chat.base.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;
import pers.hll.aigc4chat.base.constant.FilePath;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * 文件上传工具类
 *
 * @author hll
 * @since 2024/04/23
 */
@Slf4j
@UtilityClass
public class MultipartFileUtil {

    private final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd/");

    public String saveFile(MultipartFile file) throws IOException {
        String folderPath = FilePath.UPLOAD + FilePath.SEP + LocalDate.now().format(FORMATTER);
        File folder = new File(folderPath);
        if (!folder.isDirectory()) {
            folder.mkdirs();
        }
        String oldName = file.getOriginalFilename();
        assert oldName != null;
        String newName = UUID.randomUUID() + oldName.substring(oldName.lastIndexOf("."), oldName.length());
        String filePath = folderPath + newName;
        try {
            FileUtils.copyInputStreamToFile(file.getInputStream(), new File(filePath));
            return filePath;
        } catch (IOException e) {
            log.error("文件保存失败: ", e);
            throw e;
        }
    }
}
