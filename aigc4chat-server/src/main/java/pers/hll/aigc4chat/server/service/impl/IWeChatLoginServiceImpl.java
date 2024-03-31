package pers.hll.aigc4chat.server.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import pers.hll.aigc4chat.common.base.constant.FilePath;
import pers.hll.aigc4chat.common.base.util.BaseUtil;
import pers.hll.aigc4chat.common.base.util.QRCodeUtil;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.WeChatHttpClient;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.constant.WeChatCookieKey;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.constant.WeChatLoginStatus;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.request.*;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.response.JsLoginResp;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.response.LoginResp;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.response.WebWxInitResp;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.response.WxNewLoginPageResp;
import pers.hll.aigc4chat.server.converter.WeChatUserConverter;
import pers.hll.aigc4chat.server.service.IWeChatLoginService;
import pers.hll.aigc4chat.server.service.IWeChatUserService;
import pers.hll.aigc4chat.server.wechat.WeChatRequestCache;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicBoolean;

import static pers.hll.aigc4chat.common.protocol.wechat.protocol.constant.WXEndPoint.*;
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
public class IWeChatLoginServiceImpl implements IWeChatLoginService {

    private final TaskScheduler taskScheduler;

    private final AtomicBoolean resultReceived = new AtomicBoolean(false);

    private final IWeChatUserService weChatUserService;

    private void jsLogin() {
        JsLoginResp jsLoginResp = WeChatHttpClient.get(new JsLoginReq(JS_LOGIN).build());
        if (200 != jsLoginResp.getCode()) {
            throw new IllegalStateException("获取登录二维码出错");
        } else {
            String uuid = jsLoginResp.getUuid();
            WeChatRequestCache.getNeededInfo().setUuid(uuid);
            String qrCodeUri = String.format(QR_CODE, uuid);
            QRCodeUtil.writeInImageAndOpen(FilePath.WECHAT_LOGIN_QR_CODE, qrCodeUri);
        }
    }

    @Override
    public void login() {
        jsLogin();
        Runnable loginTask = () -> {
            if (!resultReceived.get()) {
                LoginResp loginResp = WeChatHttpClient.get(new LoginReq(LOGIN)
                        .setUuid(WeChatRequestCache.getNeededInfo().getUuid())
                        .build());
                // 检查响应结果是否满意，如果不满意则继续轮询
                if (isAuthorized(loginResp)) {
                    resultReceived.set(true);
                    // 处理满意的结果
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
        WeChatHttpClient.get(new WebWxLogoutReq(String.format(WEB_WX_LOGOUT, getHost()))
                .setSkey(WeChatRequestCache.getNeededInfo().getSkey())
                .build());
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
        webWxNewLoginPage(loginResp.getRedirectUri());
        initial();
    }

    /**
     * 用户登录 返回 uin sid skey 等重要信息 将其放到缓存中去
     *
     * @param url 登录url
     */
    public void webWxNewLoginPage(String url) {
        WxNewLoginPageResp wxNewLoginPageResp = WeChatHttpClient.get(new WxNewLoginPageReq(url)
                .setRedirectsEnabled(false)
                .build());
        if (wxNewLoginPageResp == null
                || StringUtils.isEmpty(wxNewLoginPageResp.getRet())
                || !"0".equals(wxNewLoginPageResp.getRet())) {
            log.error("请求新登录页面[{}]失败: {}", url, wxNewLoginPageResp);
            throw new IllegalStateException("登录失败");
        }
        WeChatRequestCache.getNeededInfo()
                .setUin(wxNewLoginPageResp.getWxUin())
                .setSid(wxNewLoginPageResp.getWxSid())
                .setSkey(wxNewLoginPageResp.getSKey())
                .setPassTicket(wxNewLoginPageResp.getPassTicket());
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
            WebWxInitResp webWxInitResp = webWxInit();
            weChatUserService.saveOrUpdate(WeChatUserConverter.from(webWxInitResp.getUser()));
            //weChatContacts.setMe(getHost(), webWxInitResp.getUser());
            //
            //// 获取并保存最近联系人
            //log.info("正在获取并保存最近联系人...");
            //loadContacts(webWxInitResp.getChatSet(), true);
            //
            //// 发送初始化状态信息
            //WebWxStatusNotifyResp webWxStatusNotifyResp =
            //        weChatApi.webWxStatusNotify(weChatContacts.getMe().getId(), WXNotify.NOTIFY_INITED);
            //log.info("状态通知结果:{}", webWxStatusNotifyResp);
            //
            //// 获取好友、保存的群聊、公众号列表。
            //// 这里获取的群没有群成员，不过也不能用fetchContact方法暴力获取群成员，因为这样数据量会很大
            //log.info("正在获取好友、群、公众号列表...");
            //WebWxGetContactResp webWxGetContactResp = weChatApi.webWxGetContact();
            //for (User user : webWxGetContactResp.getMemberList()) {
            //    weChatContacts.putContact(weChatApi.getHost(), user);
            //}
        } catch (Exception e) {
            log.error("初始化异常:", e);
        }
    }

    /**
     * 初始化，获取自身信息，好友列表，活跃群等
     *
     * @return 初始化结果
     */
    private WebWxInitResp webWxInit() {
        WebWxInitResp webWxInitResp = WeChatHttpClient.post(new WebWxInitReq(String.format(WEB_WX_INIT, getHost()))
                .setPassTicket(WeChatRequestCache.getPassTicket())
                .setBaseRequestBody(WeChatRequestCache.getBaseRequestBody())
                .build());
        log.info("初始化化结果: {}", BaseUtil.GSON.toJson(webWxInitResp));
        String skey = webWxInitResp.getSKey();
        if (skey == null) {
            WeChatRequestCache.getNeededInfo().setSkey("");
        }
        WeChatRequestCache.getNeededInfo().setSyncKey(webWxInitResp.getSyncKey());
        return webWxInitResp;
    }

    private String getHost() {
        return WeChatRequestCache.getHost();
    }
}
