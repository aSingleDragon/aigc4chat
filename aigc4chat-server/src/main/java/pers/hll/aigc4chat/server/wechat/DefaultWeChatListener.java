package pers.hll.aigc4chat.server.wechat;

import lombok.extern.slf4j.Slf4j;
import pers.hll.aigc4chat.common.entity.wechat.contact.WXContact;
import pers.hll.aigc4chat.common.entity.wechat.message.WXLocation;
import pers.hll.aigc4chat.common.entity.wechat.message.WXMessage;
import pers.hll.aigc4chat.common.entity.wechat.message.WXText;
import pers.hll.aigc4chat.common.entity.wechat.message.WXVerify;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 *
 * @author hll
 * @since 2024/03/19
 */
@Slf4j
public class DefaultWeChatListener implements WeChatListener {


    @Override
    public void onLogin(@Nonnull WeChatClient client) {
        log.info("onLogin：您有{}名好友、活跃微信群{}个", client.userFriends().size(), client.userGroups().size());
    }

    @Override
    public void onMessage(@Nonnull WeChatClient client, @Nonnull WXMessage message) {
        //log.info("获取到消息:{}", BaseUtil.GSON.toJson(message));

        if (message instanceof WXVerify wxVerify) {
            // 是好友请求消息，自动同意好友申请
            client.passVerify(wxVerify);
        } else if (message instanceof WXLocation && message.getFromUser() != null
                && !message.getFromUser().getId().equals(client.userMe().getId())) {
            // 如果对方告诉我他的位置，发送消息的不是自己，则我也告诉他我的位置
            if (message.getFromGroup() != null) {
                // 群消息
                 client.sendLocation(message.getFromGroup(), "120.14556", "30.23856", "我在这里", "西湖");
            } else {
                // 用户消息
                client.sendLocation(message.getFromUser(), "120.14556", "30.23856", "我在这里", "西湖");
            }
        } else if (message instanceof WXText && message.getFromUser() != null
                && !message.getFromUser().getId().equals(client.userMe().getId())) {
            // 是文字消息，并且发送消息的人不是自己，发送相同内容的消息
            if (message.getFromGroup() != null) {
                // 群消息
                 client.sendText(message.getFromGroup(), message.getContent());
            } else {
                // 用户消息
                client.sendText(message.getFromUser(), message.getContent());
            }
        }
    }

    @Override
    public void onContact(@Nonnull WeChatClient client, @Nullable WXContact oldContact, @Nullable WXContact newContact) {
        log.info("检测到联系人变更:旧联系人名称：{}:新联系人名称：{}",
                (oldContact == null ? "null" : oldContact.getName()), (newContact == null ? "null" : newContact.getName()));
    }
}
