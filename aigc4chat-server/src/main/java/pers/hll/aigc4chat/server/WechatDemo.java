package pers.hll.aigc4chat.server;

import lombok.extern.slf4j.Slf4j;
import pers.hll.aigc4chat.common.entity.wechat.contact.WXContact;
import pers.hll.aigc4chat.common.entity.wechat.contact.WXGroup;
import pers.hll.aigc4chat.common.entity.wechat.contact.WXUser;
import pers.hll.aigc4chat.common.entity.wechat.message.WXUnknown;
import pers.hll.aigc4chat.common.entity.wechat.message.WXVerify;
import pers.hll.aigc4chat.server.wechat.DefaultWeChatListener;
import pers.hll.aigc4chat.server.wechat.WeChatClient;

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

    public static void main(String[] args) {
        // 新建一个模拟微信客户端
        WeChatClient wechatClient = new WeChatClient();
        // 为模拟微信客户端设置监听器
        wechatClient.setListener(new DefaultWeChatListener());
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
                            log.info("success:{}", wechatClient.sendText(contact, text));
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
                            log.info("success:{}", wechatClient.sendFile(contact, file));
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
                            log.info("success:{}", wechatClient.sendLocation(contact, longitude, latitude, title, lable));
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
