package pers.hll.aigc4chat.server.service.impl;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import pers.hll.aigc4chat.common.base.util.EasyCollUtil;
import pers.hll.aigc4chat.common.base.util.StringUtil;
import pers.hll.aigc4chat.common.entity.wechat.message.WXNotify;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.WeChatHttpClient;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.constant.MsgType;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.constant.WeChatCookieKey;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.constant.WeChatLoginStatus;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.request.body.Contact;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.response.*;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.response.webwxinit.User;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.response.webwxsync.AddMsg;
import pers.hll.aigc4chat.server.converter.WeChatMessageConverter;
import pers.hll.aigc4chat.server.converter.WeChatUserConverter;
import pers.hll.aigc4chat.server.entity.WeChatGroupMember;
import pers.hll.aigc4chat.server.entity.WeChatUser;
import pers.hll.aigc4chat.server.handler.MessageHandler;
import pers.hll.aigc4chat.server.handler.MessageHandlerName;
import pers.hll.aigc4chat.server.service.*;
import pers.hll.aigc4chat.server.wechat.WeChatRequestCache;
import pers.hll.aigc4chat.server.wechat.WeChatTool;

import java.time.Duration;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static pers.hll.aigc4chat.common.protocol.wechat.protocol.constant.WeChatWebApiConstant.HOST_LIST;

/**
 * 登录服务
 *
 * @author hll
 * @since 2024/03/31
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WeChatLoginServiceImpl implements IWeChatLoginService {

    private final IWeChatUserService weChatUserService;

    private final IWeChatGroupMemberService weChatGroupMemberService;

    private final IWeChatApiService weChatApiService;

    private final IWeChatMessageService weChatMessageService;

    private final IWechatMessageHandlerConfigService wechatMessageHandlerConfigService;

    private final ApplicationContext applicationContext;

    private final TaskScheduler taskScheduler;

    private final AtomicBoolean resultReceived = new AtomicBoolean(false);

    private final AtomicInteger syncCheckErrorCount = new AtomicInteger(0);

    private static final int SYNC_CHECK_ERROR_THRESHOLD = 5;

    @Override
    public void login(HttpServletResponse response) {
        weChatApiService.jsLogin(response);
        Runnable loginTask = () -> {
            if (!resultReceived.get()) {
                LoginResp loginResp = weChatApiService.login();
                if (isAuthorized(loginResp)) {
                    resultReceived.set(true);
                    handleSuccessfulResponse(loginResp);
                }
            }
        };
        Instant timeoutInstant = Instant.now().plusSeconds(60L);
        // 每秒钟向微信服务器轮询一次用户登录状态
        ScheduledFuture<?> scheduledFuture = taskScheduler.scheduleWithFixedDelay(loginTask, Duration.ofSeconds(1L));
        Runnable timeoutTask = () -> {
            if (!resultReceived.get()) {
                // 及时取消轮询任务
                scheduledFuture.cancel(true);
                handleTimeout();
            }
        };
        // 如果一分钟了 用户还没有扫码 则执行超时任务
        taskScheduler.schedule(timeoutTask, timeoutInstant);
    }

    @Override
    public void logout() {
        weChatApiService.logout();
    }

    /**
     * 检查用户是否已经授权
     *
     * @param loginResp 登录响应
     * @return 是否授权
     */
    private boolean isAuthorized(LoginResp loginResp) {
        WeChatLoginStatus websChatLoginStatus = WeChatLoginStatus.ofCode(loginResp.getCode());
        log.info("{}", websChatLoginStatus.getMsg());
        return WeChatLoginStatus.AUTHORIZED_LOGIN == websChatLoginStatus;
    }

    /**
     * 处理用户授权后的事情
     *
     * @param loginResp 登录响应
     */
    private void handleSuccessfulResponse(LoginResp loginResp) {
        for (String host : HOST_LIST) {
            if (loginResp.getRedirectUri().contains(host)) {
                WeChatRequestCache.getNeededInfo().setHost(host);
                break;
            }
        }
        if (StringUtils.isEmpty(WeChatRequestCache.getNeededInfo().getHost())) {
            throw new IllegalStateException("未知主机: " + loginResp.getRedirectUri());
        }
        weChatApiService.webWxNewLoginPage(loginResp.getRedirectUri());
        initial();
        listen();
    }

    /**
     * 处理超时(二维码生成了一分钟了 这个集霸还tm不扫)
     */
    private void handleTimeout() {
        // ignored 忍了
    }

    private void initial() {
        try {
            if (WeChatHttpClient.getCookieStore() != null) {
                WeChatHttpClient.getCookieStore().getCookies().forEach(httpCookie -> {
                    if (WeChatCookieKey.WEB_WX_DATA_TICKET.equalsIgnoreCase(httpCookie.getName())) {
                        WeChatRequestCache.getNeededInfo().setWebWxDataTicket(httpCookie.getValue());
                        log.info("已成功设置{}:{}", WeChatCookieKey.WEB_WX_DATA_TICKET, httpCookie.getValue());
                    }
                });
            }
            // 获取自身信息
            log.info("正在获取自身信息...");
            WebWxInitResp webWxInitResp = weChatApiService.webWxInit();
            weChatUserService.saveOrUpdateMe(WeChatUserConverter.from(webWxInitResp.getUser()));
            // 获取并保存最近联系人
            log.info("正在获取并保存最近联系人...");
            loadContacts(webWxInitResp.getChatSet(), true);
            // 发送初始化状态信息
            WebWxStatusNotifyResp webWxStatusNotifyResp =
                    weChatApiService.webWxStatusNotify(weChatUserService.selectMe().getUserName(), WXNotify.NOTIFY_INITED);
            log.info("状态通知结果:{}", webWxStatusNotifyResp);
            // 获取好友、保存的群聊、公众号列表。
            // 这里获取的群没有群成员，不过也不能用fetchContact方法暴力获取群成员，因为这样数据量会很大
            log.info("正在获取好友、群、公众号列表...");
            weChatApiService.webWxGetContact();
        } catch (Exception e) {
            log.error("初始化异常:", e);
        }
    }

    public void listen() {
        Thread syncCheckThread = new Thread(() -> {
            while (syncCheckErrorCount.get() < SYNC_CHECK_ERROR_THRESHOLD) {
                SyncCheckResp syncCheckResp = weChatApiService.syncCheck();
                if (syncCheckResp != null && syncCheckResp.getRetCode() == 0) {
                    if (syncCheckResp.getSelector() > 0) {
                        WebWxSyncResp webWxSyncResp = weChatApiService.webWxSync();
                        handleDeleteContactList(webWxSyncResp.getDelContactList());
                        handleModifyContactList(webWxSyncResp.getModContactList());
                        handleAddMessage(webWxSyncResp.getAddMsgList());
                        syncCheckErrorCount.set(0);
                    }
                } else {
                    syncCheckErrorCount.incrementAndGet();
                }
            }
        });
        syncCheckThread.start();
    }

    private void handleDeleteContactList(List<User> deleteUserList) {
        // 删除好友立刻触发; 删除群后的任意一条消息触发; 被移出群不会触发（会收到一条被移出群的addMsg）
        weChatUserService.removeBatchByIds(EasyCollUtil.getFieldList(deleteUserList, User::getUserName));
    }

    private void handleAddMessage(List<AddMsg> addMsgList) {
        if (CollectionUtils.isEmpty(addMsgList)) {
            return;
        }
        weChatMessageService.saveBatch(WeChatMessageConverter.from(addMsgList));
        for (AddMsg addMsg : addMsgList) {
            log.info("收到消息:{}", addMsg);
            if (addMsg.getMsgType() == MsgType.READ
                    && (addMsg.getStatusNotifyCode() == WXNotify.NOTIFY_SYNC_CONV)) {
                // 会话同步，网页微信仅仅只获取了相关联系人详情
                loadContacts(addMsg.getStatusNotifyUserName(), false);
            }
            // 不处理自己发的消息
            if (!Objects.equals(addMsg.getFromUserName(), weChatUserService.selectMe().getUserName())) {
                String messageHandlerName = wechatMessageHandlerConfigService.getHandlerName(addMsg.getFromUserName());
                MessageHandler messageHandler = getMessageHandler(messageHandlerName);
                messageHandler.handle(addMsg);
            }
        }
    }

    private void handleModifyContactList(List<User> modifyUserList) {
        if (CollectionUtils.isEmpty(modifyUserList)) {
            return;
        }
        // 添加好友立刻触发; 被拉入已存在的群立刻触发
        // 被拉入新群第一条消息触发（同时收到2条addMsg，一条被拉入群，一条聊天消息）
        // 群里有人加入或群里踢人或修改群信息之后第一条信息触发
        for (User user : modifyUserList) {
            // 由于在这里获取到的联系人（无论是群还是用户）的信息是不全的，所以使用接口重新获取
            WeChatUser oldUser = weChatUserService.getById(user.getUserName());
            if (oldUser != null && StringUtils.isEmpty(oldUser.getNickName())) {
                weChatUserService.removeById(user.getUserName());
                oldUser = null;
            }
            WeChatUser newUser = fetchContact(user.getUserName());
            if (newUser != null && StringUtils.isEmpty(newUser.getNickName())) {
                weChatUserService.removeById(user.getUserName());
                newUser = null;
            }
            if (oldUser != null && newUser != null) {
                log.info("变更联系人: {}", user.getUserName());
                // TODO

            }
        }
    }

    public WeChatUser fetchContact(String userName) {
        loadContacts(userName, false);
        WeChatUser weChatUser = weChatUserService.getById(userName);
        if (WeChatTool.isGroup(weChatUser.getUserName())) {
            List<WeChatGroupMember> members = weChatGroupMemberService.listByGroupUserName(weChatUser.getUserName());
            List<Contact> contacts = new LinkedList<>(members
                    .stream()
                    .map(x -> new Contact(x.getUserName(), x.getGroupUserName()))
                    .toList());
            loadContacts(contacts, true);
            //weChatUser.setDetail(true);
        }
        return weChatUser;
    }

    /**
     * 获取并保存不限数量和类型的联系人信息
     *
     * @param userNames 逗号分隔的联系人userName
     */
    private void loadContacts(String userNames, boolean useCache) {
        if (StringUtils.isNotEmpty(userNames)) {
            List<Contact> contactList = StringUtil.splitToList(userNames).stream().map(Contact::new).toList();
            loadContacts(new LinkedList<>(contactList), useCache);
        }
    }

    /**
     * 获取并保存不限数量和类型的联系人信息
     *
     * @param contacts 要获取的联系人的列表，数量和类型不限
     */
    private void loadContacts(List<Contact> contacts, boolean useCache) {
        if (useCache) {
            // 不是群聊，并且已经获取过，就不再次获取
            contacts.removeIf(contact -> WeChatTool.isNotGroup(contact.getUserName())
                    && weChatUserService.getById(contact.getUserName()) != null);
        }
        // 拆分成每次50个联系人分批获取
        if (contacts.size() > 50) {
            LinkedList<Contact> temp = new LinkedList<>();
            for (Contact contact : contacts) {
                temp.add(contact);
                if (temp.size() >= 50) {
                    weChatApiService.webWxBatchGetContact(contacts);
                    temp.clear();
                }
            }
            contacts = temp;
        }
        if (!contacts.isEmpty()) {
            weChatApiService.webWxBatchGetContact(contacts);
        }
    }

    private MessageHandler getMessageHandler(String beanName) {
        if (StringUtils.isEmpty(beanName)) {
            return (MessageHandler) applicationContext.getBean(MessageHandlerName.DEFAULT_MESSAGE_HANDLER);
        }
        return (MessageHandler) applicationContext.getBean(beanName);
    }
}
