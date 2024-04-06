package pers.hll.aigc4chat.server.wechat;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.extern.slf4j.Slf4j;
import pers.hll.aigc4chat.common.entity.wechat.contact.WXContact;
import pers.hll.aigc4chat.common.entity.wechat.message.WXLocation;
import pers.hll.aigc4chat.common.entity.wechat.message.WXMessage;
import pers.hll.aigc4chat.common.entity.wechat.message.WXText;
import pers.hll.aigc4chat.common.entity.wechat.message.WXVerify;
import pers.lys.aigc4chat.common.ai.enums.ReplyModeEnum;
import pers.lys.aigc4chat.common.ai.AiModCallServiceImpl;
import pers.hll.aigc4chat.common.entity.wechat.message.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * 默认监听器 复读机
 *
 * @author hll
 * @since 2024/03/19
 */
@Slf4j
public class DefaultWeChatListener implements WeChatListener {

    @Override
    public void onMessage(WeChatClient client, WXMessage message) {

        if (message instanceof WXVerify wxVerify) {
            // 是好友请求消息，自动同意好友申请
            client.passVerify(wxVerify);
        } else if (message instanceof WXLocation wxLocation && message.getFromUser() != null
                && !message.getFromUser().getUserName().equals(client.userMe().getUserName())) {
            // 如果对方告诉我他的位置，发送消息的不是自己，则我也告诉他我的位置
            if (message.getFromGroup() != null) {
                // 群消息 默认注释 小心被喷
                 client.sendLocation(message.getFromGroup(), wxLocation.getOriContent());
            } else {
                // 用户消息
                client.sendLocation(message.getFromUser(), wxLocation.getOriContent());
            }
        } else if (message instanceof WXText && message.getFromUser() != null
                && !message.getFromUser().getUserName().equals(client.userMe().getUserName())) {

            // 获取对话结果
            AiModCallServiceImpl aiModCallService = new AiModCallServiceImpl();
            //String content = aiModCallService.call(message.getContent(), gainReplyMod(message.getFromUser().getId()));
            String content = aiModCallService.call(message.getContent(), "Agent");
            if (StringUtils.isBlank(content)) {
                return;
            }
            // 回复指定用户
            if (message.getFromGroup() != null) {
                // 群消息
                client.sendText(message.getFromGroup(), content);
            } else {
                // 用户消息
                client.sendText(message.getFromUser(), content);
            }
        } else if (message instanceof WXVoice wxVoice
                && !message.getFromUser().getUserName().equals(client.userMe().getUserName())) {
            // 是语音消息 并且发送消息的人不是自己 发送相同内容的消息
            if (message.getFromGroup() != null) {
                // 群消息 默认注释 小心被喷
                client.sendFile(message.getFromGroup(), wxVoice.getVoice());
            } else {
                // 用户消息
                //client.sendFile(message.getFromUser(), wxVoice.getVoice());
                client.sendVoice(message.getFromUser(), wxVoice.getVoice());
            }
        } else if (message instanceof WXEmoji wxEmoji
                && !message.getFromUser().getUserName().equals(client.userMe().getUserName())) {
            // 是语音消息 并且发送消息的人不是自己 发送相同内容的消息
            if (message.getFromGroup() != null) {
                // 群消息 默认注释 小心被喷
                // do nothing
            } else {
                // 用户消息
                //client.sendEmoji(message.getFromUser(), message.getContent());
            }
        } else if (message instanceof WXImage wxImage
                && !message.getFromUser().getUserName().equals(client.userMe().getUserName())) {
            if (message.getFromGroup() != null) {
                // do nothing
                client.sendFile(message.getFromGroup(), wxImage.getImage());
            } else {
                // 用户消息
                client.sendFile(message.getFromUser(), wxImage.getImage());
            }
        }
    }

    @Override
    public void onContact(WeChatClient client, WXContact oldContact, WXContact newContact) {
        log.info("检测到联系人变更:旧联系人名称：{}:新联系人名称：{}",
                (oldContact == null ? "null" : oldContact.getNickName()), (newContact == null ? "null" : newContact.getNickName()));
    }

    /**
     * 获取回复模型
     *
     * @param userId 用户id
     * @return 模型code
     */

    public String gainReplyMod(String userId) {
        // 甄别是否要对其回复
        List<String> list = getReplyFriend();
        String modCode = null;
        if (CollectionUtils.isNotEmpty(list)) {
            for (String code : list) {
                String replyId = code.split(" ")[0];
                if (replyId.equals(userId)) {
                    modCode = code.split(" ")[1];
                    if (StringUtils.isBlank(modCode)) {
                        // 没配置模型，默认走复读机模式
                        modCode = ReplyModeEnum.REPEATER.getCode();
                    }
                    break;
                }
            }
        }
        return modCode;
    }

    /**
     * 获取需要指定回复用户id
     * 数据库还没得，先拿txt将就一下
     *
     * @return 临时配置列表
     */
    public List<String> getReplyFriend() {
        String filePath = "file/reply.txt";
        List<String> replyIds = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                replyIds.add(line);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return replyIds;
    }
}
