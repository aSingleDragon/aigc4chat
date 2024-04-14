package pers.hll.aigc4chat.common.protocol.wechat.protocol.request.form;

import org.apache.http.entity.ContentType;

/**
 * 表单文件信息
 *
 * @param formKey 表单的key
 * @param fileName 文件的名字
 * @param fileBytes 文件的byte
 */
public record FormFile(String formKey, ContentType contentType, String fileName, byte[] fileBytes) {
}
