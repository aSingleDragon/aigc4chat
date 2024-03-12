package pers.hll.aigc4chat.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import lombok.extern.slf4j.Slf4j;
import pers.hll.aigc4chat.common.entity.wechat.contact.WXContact;
import pers.hll.aigc4chat.common.entity.wechat.contact.WXGroup;
import pers.hll.aigc4chat.common.entity.wechat.contact.WXUser;
import pers.hll.aigc4chat.common.entity.wechat.message.*;
import pers.hll.aigc4chat.server.wechat.WeChatClient;


import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.util.Scanner;

/**
 * 
 * 
 * @author hll
 * @since 2024/03/10
 */
@Slf4j
public class WechatDemo {
    
    public static final Gson GSON = new GsonBuilder().disableHtmlEscaping().create();

    /**
     * 新建一个微信监听器
     */
    public static final WeChatClient.WeChatListener LISTENER = new WeChatClient.WeChatListener() {


        @Override
        public void onLogin(@Nonnull WeChatClient client) {
            log.info("onLogin：您有{}名好友、活跃微信群{}个", client.userFriends().size(), client.userGroups().size());
        }

        @Override
        public void onMessage(@Nonnull WeChatClient client, @Nonnull WXMessage message) {
            log.info("获取到消息:{}", GSON.toJson(message));

            if (message instanceof WXVerify wxVerify) {
                // 是好友请求消息，自动同意好友申请
                client.passVerify(wxVerify);
            } else if (message instanceof WXLocation && message.getFromUser() != null
                    && !message.getFromUser().getId().equals(client.userMe().getId())) {
                // 如果对方告诉我他的位置，发送消息的不是自己，则我也告诉他我的位置
                if (message.getFromGroup() != null) {
                    // 群消息
                    // client.sendLocation(message.fromGroup, "120.14556", "30.23856", "我在这里", "西湖");
                } else {
                    // 用户消息
                    client.sendLocation(message.getFromUser(), "120.14556", "30.23856", "我在这里", "西湖");
                }
            } else if (message instanceof WXText && message.getFromUser() != null
                    && !message.getFromUser().getId().equals(client.userMe().getId())) {
                // 是文字消息，并且发送消息的人不是自己，发送相同内容的消息
                if (message.getFromGroup() != null) {
                    // 群消息
                    // client.sendText(message.fromGroup, message.content);
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
    };

    public static void main(String[] args) {
        // 新建一个模拟微信客户端
        WeChatClient wechatClient = new WeChatClient();
        // 为模拟微信客户端设置监听器
        wechatClient.setListener(LISTENER);
        // 启动模拟微信客户端
        wechatClient.startup();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                log.info("请输入指令");
                switch (scanner.nextLine()) {
                    case "sendText": {
                        log.info("toContactId:");
                        String toContactId = scanner.nextLine();
                        log.info("textContent:");
                        String text = scanner.nextLine();
                        WXContact contact = wechatClient.userContact(toContactId);
                        if (contact != null) {
                            log.info("success:{}", GSON.toJson(wechatClient.sendText(contact, text)));
                        } else {
                            log.info("联系人[{}]未找到!", toContactId);
                        }
                    }
                    break;
                    case "sendFile": {
                        log.info("toContactId:");
                        String toContactId = scanner.nextLine();
                        log.info("filePath:");
                        File file = new File(scanner.nextLine());
                        WXContact contact = wechatClient.userContact(toContactId);
                        if (contact != null) {
                            log.info("success:" + GSON.toJson(wechatClient.sendFile(contact, file)));
                        } else {
                            log.info("联系人[{}]未找到!", toContactId);
                        }
                    }
                    break;
                    case "sendLocation": {
                        log.info("toContactId:");
                        String toContactId = scanner.nextLine();
                        log.info("longitude:");
                        String longitude = scanner.nextLine();
                        log.info("latitude:");
                        String latitude = scanner.nextLine();
                        log.info("title:");
                        String title = scanner.nextLine();
                        log.info("lable:");
                        String lable = scanner.nextLine();
                        WXContact contact = wechatClient.userContact(toContactId);
                        if (contact != null) {
                            log.info("success:" + GSON.toJson(wechatClient.sendLocation(contact, longitude, latitude, title, lable)));
                        } else {
                            log.info("联系人未找到");
                        }
                    }
                    break;
                    case "revokeMsg": {
                        log.info("toContactId:");
                        String toContactId = scanner.nextLine();
                        log.info("clientMsgId:");
                        String clientMsgId = scanner.nextLine();
                        log.info("serverMsgId:");
                        String serverMsgId = scanner.nextLine();
                        WXUnknown wxUnknown = new WXUnknown();
                        wxUnknown.setId(Long.parseLong(serverMsgId));
                        wxUnknown.setIdLocal(Long.parseLong(clientMsgId));
                        wxUnknown.setToContact(wechatClient.userContact(toContactId));
                        wechatClient.revokeMsg(wxUnknown);
                    }
                    break;
                    case "passVerify": {
                        log.info("userId:");
                        String userId = scanner.nextLine();
                        log.info("verifyTicket:");
                        String verifyTicket = scanner.nextLine();
                        WXVerify wxVerify = new WXVerify();
                        wxVerify.setUserId(userId);
                        wxVerify.setTicket(verifyTicket);
                        wechatClient.passVerify(wxVerify);
                    }
                    break;
                    case "editRemark": {
                        log.info("userId:");
                        String userId = scanner.nextLine();
                        log.info("remarkName:");
                        String remark = scanner.nextLine();
                        WXContact contact = wechatClient.userContact(userId);
                        if (contact instanceof WXUser) {
                            wechatClient.editRemark((WXUser) contact, remark);
                        } else {
                            log.info("好友未找到");
                        }
                    }
                    break;
                    case "topContact": {
                        log.info("contactId:");
                        String contactId = scanner.nextLine();
                        log.info("isTop:");
                        String isTop = scanner.nextLine();
                        WXContact contact = wechatClient.userContact(contactId);
                        if (contact != null) {
                            wechatClient.topContact(contact, Boolean.parseBoolean(isTop.toLowerCase()));
                        } else {
                            log.info("联系人未找到");
                        }
                    }
                    break;
                    case "setGroupName": {
                        log.info("groupId:");
                        String groupId = scanner.nextLine();
                        log.info("name:");
                        String name = scanner.nextLine();
                        WXGroup group = wechatClient.userGroup(groupId);
                        if (group != null) {
                            wechatClient.setGroupName(group, name);
                        } else {
                            log.info("群组未找到");
                        }
                    }
                    break;
                    case "quit": {
                        log.info("logging out");
                        wechatClient.shutdown();
                    }
                    return;
                    default: {
                        log.info("未知指令");
                    }
                    break;
                }
            } catch (Exception e) {
                log.error("", e);
            }
        }
    }

}
