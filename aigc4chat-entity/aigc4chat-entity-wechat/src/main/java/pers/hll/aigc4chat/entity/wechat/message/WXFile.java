package pers.hll.aigc4chat.entity.wechat.message;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.File;

/**
 * 微信文件消息
 *
 * @author hll
 * @since 2024/03/10
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WXFile extends WXMessage {

    /**
     * 文件id，用于获取文件
     */
    private String fileId;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 文件大小
     */
    private long fileSize;

    /**
     * 文件内容，刚开始为null，需要手动获取之后才会有内容
     */
    private File file;
}
