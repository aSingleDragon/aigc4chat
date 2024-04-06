package pers.hll.aigc4chat.server.service;

import pers.hll.aigc4chat.common.protocol.wechat.protocol.request.body.Contact;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.response.*;

import java.util.List;

/**
 *
 * @author hll
 * @since 2023/04/06
 */
public interface IWeChatApiService {

    void jsLogin();

    LoginResp login();

    void webWxGetContact();

    void webWxBatchGetContact(List<Contact> contactList);

    WebWxStatusNotifyResp webWxStatusNotify(String userName, int notifyCode);

    WebWxInitResp webWxInit();

    SyncCheckResp syncCheck();

    WebWxSyncResp webWxSync();

    void webWxNewLoginPage(String url);

    void logout();
}
