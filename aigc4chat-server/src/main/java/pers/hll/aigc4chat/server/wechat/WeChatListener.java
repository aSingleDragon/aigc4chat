package pers.hll.aigc4chat.server.wechat;

import pers.hll.aigc4chat.common.entity.wechat.contact.WXContact;
import pers.hll.aigc4chat.common.entity.wechat.message.WXMessage;

import javax.annotation.Nullable;

/**
 * 模拟网页微信客户端监听器
 *
 * @author hll
 * @since 2024/03/19
 */
public interface WeChatListener {

    /**
     * 获取到用户登录的二维码
     *
     * @param client 微信客户端
     * @param qrCode 用户登录二维码的url
     */
    default void onQrCode(WeChatClient client, String qrCode) {
        // ignored
    }

    /**
     * 获取用户头像，base64编码
     *
     * @param client       微信客户端
     * @param base64Avatar base64编码的用户头像
     */
    default void onAvatar(WeChatClient client, String base64Avatar) {
        // ignored
    }

    /**
     * 模拟网页微信客户端异常退出
     *
     * @param client 微信客户端
     * @param reason 错误原因
     */
    default void onFailure(WeChatClient client, String reason) {
        client.dump();
    }

    /**
     * 用户登录并初始化成功
     *
     * @param client 微信客户端
     */
    default void onLogin(WeChatClient client) {
        // ignored
    }

    /**
     * 用户获取到消息
     *
     * @param client  微信客户端
     * @param message 用户获取到的消息
     */
    default void onMessage(WeChatClient client, WXMessage message) {
        // ignored
    }

    /**
     * 用户联系人变化
     *
     * @param client     微信客户端
     * @param oldContact 旧联系人，新增联系人时为null
     * @param newContact 新联系人，删除联系人时为null
     */
    default void onContact(WeChatClient client, @Nullable WXContact oldContact, @Nullable WXContact newContact) {
        // ignored
    }

    /**
     * 模拟网页微信客户端正常退出
     *
     * @param client 微信客户端
     */
    default void onLogout(WeChatClient client) {
        client.dump();
    }
}