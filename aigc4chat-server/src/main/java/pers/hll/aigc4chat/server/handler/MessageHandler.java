package pers.hll.aigc4chat.server.handler;

import pers.hll.aigc4chat.protocol.wechat.constant.MsgType;
import pers.hll.aigc4chat.protocol.wechat.response.webwxsync.AddMsg;

/**
 * 消息处理器接口
 *
 * @author hll
 * @since 2024/04/21
 */
public interface MessageHandler {

    default boolean isTextMessage(AddMsg addMsg) {
        return addMsg.getMsgType() == MsgType.TEXT && addMsg.getSubMsgType() == 0;
    }

    default boolean isLocationMessage(AddMsg addMsg) {
        return addMsg.getMsgType() == MsgType.TEXT && addMsg.getSubMsgType() == MsgType.LOCATION;
    }

    default boolean isImageMessage(AddMsg addMsg) {
        return addMsg.getMsgType() == MsgType.IMAGE;
    }

    default boolean isVoiceMessage(AddMsg addMsg) {
        return addMsg.getMsgType() == MsgType.VOICE;
    }

    default boolean isVerifyMessage(AddMsg addMsg) {
        return addMsg.getMsgType() == MsgType.VERIFY;
    }

    default boolean isRecommendMessage(AddMsg addMsg) {
        return addMsg.getMsgType() == MsgType.RECOMMEND;
    }

    default boolean isVideoMessage(AddMsg addMsg) {
        return addMsg.getMsgType() == MsgType.VIDEO;
    }

    default boolean isEmojiMessage(AddMsg addMsg) {
        // 如果 msg.getContent()) || msg.getHasProductId() != 0
        // 则表示这个表情包是表情商店的 无法下载
        return addMsg.getMsgType() == MsgType.EMOJI;
    }

    default boolean isAppMessage(AddMsg addMsg) {
        return addMsg.getMsgType() == MsgType.OTHER;
    }

    default boolean isReadMessage(AddMsg addMsg) {
        return addMsg.getMsgType() == MsgType.READ;
    }

    default boolean isSystemMessage(AddMsg addMsg) {
        return addMsg.getMsgType() == MsgType.SYSTEM;
    }

    default boolean isRevokeMessage(AddMsg addMsg) {
        return addMsg.getMsgType() == MsgType.REVOKE;
    }

    default void handleTextMessage(AddMsg addMsg) {

    }

    default void handleLocationMessage(AddMsg addMsg) {

    }

    default void handleLocalMessage(AddMsg addMsg) {

    }

    default void handleRevokeMsg(AddMsg addMsg) {

    }

    default void handle(AddMsg addMsg) {
        if (isTextMessage(addMsg)) {
            handleTextMessage(addMsg);
            return;
        }
        if (isLocationMessage(addMsg)) {
            handleTextMessage(addMsg);
            return;
        }
        if (isRevokeMessage(addMsg)) {
            handleRevokeMsg(addMsg);
        }
    }
}
