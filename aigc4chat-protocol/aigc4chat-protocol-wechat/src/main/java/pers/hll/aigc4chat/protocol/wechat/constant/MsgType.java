package pers.hll.aigc4chat.protocol.wechat.constant;

/**
 * 微信消息的类型
 *
 * @author hll
 * @since 2024/03/19
 */
public final class MsgType {

    private MsgType() {}

    /**
     * 文字消息
     */
    public static final int TEXT = 1;

    /**
     * 图片消息
     */
    public static final int IMAGE = 3;

    /**
     * 语音消息
     */
    public static final int VOICE = 34;

    /**
     * 好友请求
     */
    public static final int VERIFY = 37;

    /**
     * 名片消息
     */
    public static final int RECOMMEND = 42;

    /**
     * 视频消息
     */
    public static final int VIDEO = 43;

    /**
     * 收藏的表情
     */
    public static final int EMOJI = 47;

    /**
     * 定位消息
     */
    public static final int LOCATION = 48;

    /**
     * 转账、文件、链接、笔记等
     */
    public static final int OTHER = 49;

    /**
     * 消息已读
     */
    public static final int READ = 51;

    /**
     * 系统消息
     */
    public static final int SYSTEM = 10000;

    /**
     * 系统消息，撤回消息
     */
    public static final int REVOKE = 10002;
}
