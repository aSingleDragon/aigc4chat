package pers.hll.aigc4chat.protocol.wechat.request.form;

import org.apache.http.entity.ContentType;

import java.util.Arrays;
import java.util.Objects;

/**
 * 表单文件信息
 *
 * @param formKey 表单的key
 * @param contentType 文件类型
 * @param fileName 文件的名字
 * @param fileBytes 文件的byte
 */
public record FormFile(String formKey, ContentType contentType, String fileName, byte[] fileBytes) {

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FormFile formFile)) {
            return false;
        }
        return Objects.equals(formKey, formFile.formKey)
                && Objects.equals(fileName, formFile.fileName)
                && Objects.deepEquals(fileBytes, formFile.fileBytes)
                && Objects.equals(contentType, formFile.contentType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(formKey, contentType, fileName, Arrays.hashCode(fileBytes));
    }

    @Override
    public String toString() {
        return "FormFile{" +
                "formKey='" + formKey + '\'' +
                ", contentType=" + contentType +
                ", fileName='" + fileName + '\'' +
                ", fileBytes=" + Arrays.toString(fileBytes) +
                '}';
    }
}
