package pers.hll.aigc4chat.server.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import pers.hll.aigc4chat.common.base.constant.FilePath;
import pers.hll.aigc4chat.common.base.util.BaseUtil;
import pers.hll.aigc4chat.common.base.util.QRCodeUtil;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.WeChatHttpClient;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.request.*;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.request.body.Contact;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.response.*;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.response.webwxinit.User;
import pers.hll.aigc4chat.server.converter.WeChatGroupMemberConverter;
import pers.hll.aigc4chat.server.converter.WeChatUserConverter;
import pers.hll.aigc4chat.server.entity.WeChatGroupMember;
import pers.hll.aigc4chat.server.entity.WeChatUser;
import pers.hll.aigc4chat.server.service.IWeChatApiService;
import pers.hll.aigc4chat.server.service.IWeChatGroupMemberService;
import pers.hll.aigc4chat.server.service.IWeChatUserService;
import pers.hll.aigc4chat.server.wechat.WeChatRequestCache;
import pers.hll.aigc4chat.server.wechat.WeChatTool;

import java.util.List;

import static pers.hll.aigc4chat.common.protocol.wechat.protocol.constant.WXEndPoint.*;

/**
 *
 * @author hll
 * @since 2024/04/06
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WeChatApiServiceImpl implements IWeChatApiService {

    private final IWeChatUserService weChatUserService;

    private final IWeChatGroupMemberService weChatGroupMemberService;

    @Override
    public void jsLogin() {
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
    public LoginResp login() {
        return WeChatHttpClient.get(new LoginReq(LOGIN)
                .setUuid(WeChatRequestCache.getNeededInfo().getUuid())
                .build());
    }

    /**
     * 获取联系人列表
     */
    @Override
    public void webWxGetContact() {
        WebWxGetContactResp resp = WeChatHttpClient.get(new WebWxGetContactReq(String.format(WEB_WX_GET_GET_CONTACT, getHost()))
                .setSeq(0)
                .setSkey(WeChatRequestCache.getNeededInfo().getSkey())
                .setPassTicket(WeChatRequestCache.getPassTicket())
                .build());
        if (resp != null && resp.isSuccess()) {
            List<WeChatUser> userList = WeChatUserConverter.from(resp.getMemberList());
            weChatUserService.saveOrUpdateBatch(userList, 50);
        }
    }

    @Override
    public void webWxBatchGetContact(List<Contact> contactList) {
        String url = String.format(WEB_WX_BATCH_GET_CONTACT, getHost());
        WebWxBatchGetContactResp resp = WeChatHttpClient.post(new WebWxBatchGetContactReq(url)
                .setPassTicket(WeChatRequestCache.getNeededInfo().getPassTicket())
                .setCount(contactList.size())
                .setList(contactList)
                .setBaseRequestBody(WeChatRequestCache.getBaseRequestBody())
                .build());
        if (resp != null && resp.isSuccess()) {
            List<User> userList = resp.getContactList();
            for (User user : userList) {
                if (WeChatTool.isGroup(user.getUserName())) {
                    List<WeChatGroupMember> memberList =
                            WeChatGroupMemberConverter.from(user.getMemberList(), user.getUserName());
                    weChatGroupMemberService.saveOrUpdateBatch(memberList);
                }
            }
            weChatUserService.saveOrUpdateBatch(WeChatUserConverter.from(userList), 50);
        }
    }

    /**
     * 状态更新接口，登录登出，消息已读
     *
     * @param userName   目标联系人userName
     * @param notifyCode 状态码
     * @return 接口调用结果
     */
    @Override
    public WebWxStatusNotifyResp webWxStatusNotify(String userName, int notifyCode) {
        return WeChatHttpClient.post(new WebWxStatusNotifyReq(String.format(WEB_WX_STATUS_NOTIFY, getHost()))
                .setPassTicket(WeChatRequestCache.getNeededInfo().getPassTicket())
                .setBaseRequestBody(WeChatRequestCache.getBaseRequestBody())
                .setNotifyCode(notifyCode)
                .setUserName(userName)
                .build());
    }

    /**
     * 初始化，获取自身信息，好友列表，活跃群等
     *
     * @return 初始化结果
     */
    @Override
    public WebWxInitResp webWxInit() {
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

    /**
     * 同步检查接口，需要无限循环请求该接口，如果有消息要同步，则该接口立即返回并携带参数，否则将在60秒后返回
     *
     * @return 检查结果
     */
    @Override
    public SyncCheckResp syncCheck() {
        WeChatRequestCache.NeededInfo neededInfo = WeChatRequestCache.getNeededInfo();
        neededInfo.setTime(neededInfo.getTime() + 1);
        return WeChatHttpClient.get(new SyncCheckReq(String.format(SYNC_CHECK, getHost()))
                .setSkey(neededInfo.getSkey())
                .setSid(neededInfo.getSid())
                .setUin(neededInfo.getUin())
                .setDeviceId(BaseUtil.createDeviceId())
                .setSyncKey(neededInfo.getSyncCheckKey() != null
                        ? neededInfo.getSyncCheckKey()
                        : neededInfo.getSyncKey())
                .setLoginTime(neededInfo.getTime())
                .build());
    }

    /**
     * 同步接口，将服务端数据同步到本地，并更新本地SyncKey
     *
     * @return 获取到的数据
     */
    @Override
    public WebWxSyncResp webWxSync() {
        WeChatRequestCache.NeededInfo neededInfo = WeChatRequestCache.getNeededInfo();
        WebWxSyncResp webWxSyncResp = WeChatHttpClient.post(new WebWxSyncReq(String.format(WEB_WX_SYNC, getHost()))
                .setSid(neededInfo.getSid())
                .setSkey(neededInfo.getSkey())
                .setPassTicket(neededInfo.getPassTicket())
                .setSyncKey(neededInfo.getSyncCheckKey() != null ? neededInfo.getSyncCheckKey() : neededInfo.getSyncKey())
                .setBaseRequestBody(WeChatRequestCache.getBaseRequestBody())
                .build());
        if (webWxSyncResp.getSyncKey() != null
                && webWxSyncResp.getSyncKey().getList() != null
                && webWxSyncResp.getSyncKey().getCount() > 0) {
            neededInfo.setSyncKey(webWxSyncResp.getSyncKey());
        }
        if (webWxSyncResp.getSyncCheckKey() != null
                && webWxSyncResp.getSyncCheckKey().getList() != null
                && webWxSyncResp.getSyncCheckKey().getCount() > 0) {
            neededInfo.setSyncCheckKey(webWxSyncResp.getSyncCheckKey());
        }
        return webWxSyncResp;
    }

    /**
     * 用户登录 返回 uin sid skey 等重要信息 将其放到缓存中去
     *
     * @param url 登录url
     */
    @Override
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

    @Override
    public void logout() {
        WeChatHttpClient.get(new WebWxLogoutReq(String.format(WEB_WX_LOGOUT, getHost()))
                .setSkey(WeChatRequestCache.getNeededInfo().getSkey())
                .build());
    }

    private String getHost() {
        return WeChatRequestCache.getHost();
    }
}
