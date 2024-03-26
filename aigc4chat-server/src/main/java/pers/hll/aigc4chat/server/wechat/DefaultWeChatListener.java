package pers.hll.aigc4chat.server.wechat;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import pers.hll.aigc4chat.common.base.util.XmlUtil;
import pers.hll.aigc4chat.common.entity.wechat.contact.WXContact;
import pers.hll.aigc4chat.common.entity.wechat.message.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * 默认监听器 复读机
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
        if (message instanceof WXVerify wxVerify) {
            // 是好友请求消息，自动同意好友申请
            client.passVerify(wxVerify);
        } else if (message instanceof WXLocation wxLocation && message.getFromUser() != null
                && !message.getFromUser().getId().equals(client.userMe().getId())) {
            // 如果对方告诉我他的位置，发送消息的不是自己，则我也告诉他我的位置
            if (message.getFromGroup() != null) {
                // 群消息 默认注释 小心被喷
                // client.sendLocation(message.getFromGroup(), wxLocation.getOriContent());
            } else {
                // 用户消息
                client.sendLocation(message.getFromUser(), wxLocation.getOriContent());
            }
        } else if (message instanceof WXText && message.getFromUser() != null
                && !message.getFromUser().getId().equals(client.userMe().getId())) {
            // 是文字消息，并且发送消息的人不是自己，发送相同内容的消息
            if (message.getFromGroup() != null) {
                // 群消息 默认注释 小心被喷
                // client.sendText(message.getFromGroup(), message.getContent());
            } else {
                // 用户消息
                client.sendText(message.getFromUser(), message.getContent());
            }
        } else if (message instanceof WXVoice wxVoice
                && !message.getFromUser().getId().equals(client.userMe().getId())) {
            // 是语音消息 并且发送消息的人不是自己 发送相同内容的消息
            if (message.getFromGroup() != null) {
                // 群消息 默认注释 小心被喷
                //client.sendFile(message.getFromGroup(), wxVoice.getVoice());
            } else {
                // 用户消息
                //client.sendFile(message.getFromUser(), wxVoice.getVoice());
                client.sendVoice(message.getFromUser(), wxVoice.getVoice());
            }
        } else if (message instanceof WXEmoji wxEmoji
                && !message.getFromUser().getId().equals(client.userMe().getId())) {
            // 是语音消息 并且发送消息的人不是自己 发送相同内容的消息
            if (message.getFromGroup() != null) {
                // 群消息 默认注释 小心被喷
                // do nothing
            } else {
                // 用户消息
                //client.sendEmoji(message.getFromUser(), message.getContent());
            }
        } else if (message instanceof WXImage wxImage
                && !message.getFromUser().getId().equals(client.userMe().getId())) {
            if (message.getFromGroup() != null) {
                // do nothing
                //client.sendFile(message.getFromGroup(), wxImage.getImage());
            } else {
                // 用户消息
                client.sendFile(message.getFromUser(), wxImage.getImage());
            }
        }
    }

    @Override
    public void onContact(@Nonnull WeChatClient client, @Nullable WXContact oldContact, @Nullable WXContact newContact) {
        log.info("检测到联系人变更:旧联系人名称：{}:新联系人名称：{}",
                (oldContact == null ? "null" : oldContact.getName()), (newContact == null ? "null" : newContact.getName()));
    }
}
