package pers.hll.aigc4chat.server.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 微信消息实体类
 *
 * @author hll
 * @since 2024/03/19
 */
@Data
@TableName("wechat_message")
@EqualsAndHashCode(callSuper = true)
public class WeChatMessage extends BaseEntity {

    /**
     * 消息ID
     */
    @TableId
    private Long msgId;

    /**
     * 发送者用户名
     */
    private String fromUserName;

    /**
     * 接收者用户名
     */
    private String toUserName;

    /**
     * 消息类型（文本、图片、语音、视频等）
     */
    private int msgType;

    /**
     * 消息内容（文本消息时有效）
     */
    private String content;

    /**
     * 消息状态
     */
    private long status;

    /**
     * 图片状态
     */
    private long imgStatus;

    /**
     * 消息创建时间（Unix时间戳）
     */
    private long createTime;

    /**
     * 语音消息长度（单位：秒）
     */
    private long voiceLength;

    /**
     * 音频播放长度
     */
    private int playLength;

    /**
     * 文件名（附件消息时有效）
     */
    private String fileName;

    /**
     * 文件大小（附件消息时有效）
     */
    private String fileSize;

    /**
     * 媒体资源ID（多媒体消息时有效）
     */
    private String mediaId;

    /**
     * 资源URL（多媒体消息时有效）
     */
    private String url;

    /**
     * 应用消息类型
     */
    private int appMsgType;

    /**
     * 状态通知代码
     */
    private int statusNotifyCode;

    /**
     * 状态通知用户名
     */
    private String statusNotifyUserName;

    /**
     * 推荐信息（如果有推荐信息时存在）
     */
    //private RecommendInfo recommendInfo;

    /**
     * 是否转发消息标志
     */
    private int forwardFlag;

    /**
     * 应用信息（应用消息时存在）
     */
    //private AppInfo appInfo;

    /**
     * 是否包含商品ID标志
     */
    private int hasProductId;

    /**
     * 验证票据（用于获取临时素材时使用）
     */
    private String ticket;

    /**
     * 图片高度（图片消息时有效）
     */
    private int imgHeight;

    /**
     * 图片宽度（图片消息时有效）
     */
    private int imgWidth;

    /**
     * 子消息类型
     */
    private int subMsgType;

    /**
     * 新消息ID（主要用于撤回/重发消息时使用）
     */
    private long newMsgId;

    /**
     * 原始内容（用于撤回/编辑消息时使用）
     */
    private String oriContent;

    /**
     * 加密后的文件名（加密消息时有效）
     */
    private String encrFileName;
}