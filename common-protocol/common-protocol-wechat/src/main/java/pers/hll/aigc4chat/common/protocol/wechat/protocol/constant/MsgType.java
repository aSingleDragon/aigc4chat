package pers.hll.aigc4chat.common.protocol.wechat.protocol.constant;

public interface MsgType {

    /**
     * 文字消息
     */
    int TYPE_TEXT = 1;

    /**
     * 图片消息
     */
    int TYPE_IMAGE = 3;

    /**
     * 语音消息
     */
    int TYPE_VOICE = 34;

    /**
     * 好友请求
     */
    int TYPE_VERIFY = 37;

    /**
     * 名片消息
     */
    int TYPE_RECOMMEND = 42;

    /**
     * 视频消息
     */
    int TYPE_VIDEO = 43;

    /**
     * 收藏的表情
     */
    int TYPE_EMOJI = 47;

    /**
     * 定位消息
     */
    int TYPE_LOCATION = 48;

    /**
     * 转账、文件、链接、笔记等
     */
    int TYPE_OTHER = 49;

    /**
     * 消息已读
     */
    int TYPE_NOTIFY = 51;

    /**
     * 系统消息
     */
    int TYPE_SYSTEM = 10000;

    /**
     * 系统消息，撤回消息
     */
    int TYPE_REVOKE = 10002;
}
