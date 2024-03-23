//package pers.hll.aigc4chat.server.wechat;
//
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//
///**
// * 模拟网页微信客户端工作线程
// *
// * @author hll
// * @since 2024/03/22
// */
//@Slf4j
//public class WeChatThread extends Thread {
//
//    @Override
//    public void run() {
//        int loginCount = 0;
//        while (!isInterrupted()) {
//            // 用户登录
//            log.info("正在登录");
//            String loginErr = login();
//            if (StringUtils.isNotEmpty(loginErr)) {
//                log.error("登录出现错误: {}", loginErr);
//                handleFailure(loginErr);
//            }
//            // 用户初始化
//            log.info("开始初始化...");
//            String initErr = initial();
//            if (StringUtils.isNotEmpty(initErr)) {
//                log.error("初始化出现错误：{}", initErr);
//                handleFailure(initErr);
//                break;
//            }
//            handleLogin();
//            // 同步消息
//            log.info("开始监听消息...");
//            String listenErr = listen();
//            if (StringUtils.isNotEmpty(listenErr)) {
//                if (loginCount++ > 10) {
//                    handleFailure(listenErr);
//                    break;
//                } else {
//                    continue;
//                }
//            }
//            // 退出登录
//            log.info("正在退出登录");
//            handleLogout();
//            break;
//        }
//    }
//
//    /**
//     * 用户登录
//     *
//     * @return 登录时异常原因，为null表示正常登录
//     */
//    @Nullable
//    private String login() {
//        try {
//            if (StringUtils.isEmpty(weChatApi.getSid())) {
//                String qrCode = weChatApi.jsLogin();
//                handleQRCode(qrCode);
//                while (true) {
//                    LoginResp loginResp = weChatApi.login();
//                    switch (loginResp.getCode()) {
//                        case 200:
//                            log.info("已授权登录");
//                            weChatApi.webWxNewLoginPage(loginResp.getRedirectUri());
//                            return null;
//                        case 201:
//                            log.info("已扫描二维码");
//                            handleAvatar(loginResp.getUserAvatar());
//                            break;
//                        case 408:
//                            log.info("等待授权登录");
//                            break;
//                        default:
//                            log.warn("登录超时...");
//                            return LOGIN_TIMEOUT;
//                    }
//                }
//            } else {
//                return null;
//            }
//        } catch (Exception e) {
//            log.error("登录异常:", e);
//            return LOGIN_EXCEPTION;
//        }
//    }
//
//    /**
//     * 初始化
//     *
//     * @return 初始化异常原因，为null表示正常初始化
//     */
//    @Nullable
//    private String initial() {
//        try {
//            // 通过Cookie获取重要参数
//            //log.info("正在获取Cookie");
//            //for (HttpCookie cookie : XHttpTools.EXECUTOR.getCookies()) {
//            //    if ("wxsid".equalsIgnoreCase(cookie.getName())) {
//            //        weChatApi.setSid(cookie.getValue());
//            //    } else if ("wxuin".equalsIgnoreCase(cookie.getName())) {
//            //        weChatApi.setUin(cookie.getValue());
//            //    } else if ("webwx_data_ticket".equalsIgnoreCase(cookie.getName())) {
//            //        weChatApi.setDataTicket(cookie.getValue());
//            //    }
//            //}
//
//            // 获取自身信息
//            log.info("正在获取自身信息...");
//            WebWxInitResp webWxInitResp = weChatApi.webWxInit();
//            weChatContacts.setMe(weChatApi.getHost(), webWxInitResp.getUser());
//
//            // 获取并保存最近联系人
//            log.info("正在获取并保存最近联系人...");
//            loadContacts(webWxInitResp.getChatSet(), true);
//
//            // 发送初始化状态信息
//            WebWxStatusNotifyResp webWxStatusNotifyResp =
//                    weChatApi.webWxStatusNotify(weChatContacts.getMe().getId(), WXNotify.NOTIFY_INITED);
//            log.info("状态通知结果:{}", webWxStatusNotifyResp);
//
//            // 获取好友、保存的群聊、公众号列表。
//            // 这里获取的群没有群成员，不过也不能用fetchContact方法暴力获取群成员，因为这样数据量会很大
//            log.info("正在获取好友、群、公众号列表...");
//            WebWxGetContactResp webWxGetContactResp = weChatApi.webWxGetContact();
//            for (User user : webWxGetContactResp.getMemberList()) {
//                weChatContacts.putContact(weChatApi.getHost(), user);
//            }
//            return null;
//        } catch (Exception e) {
//            log.error("初始化异常:", e);
//            return INIT_EXCEPTION;
//        }
//    }
//
//    /**
//     * 循环同步消息
//     *
//     * @return 同步消息的异常原因，为null表示正常结束
//     */
//    @Nullable
//    private String listen() {
//        int retryCount = 0;
//        try {
//            while (!isInterrupted()) {
//                SyncCheckResp syncCheckResp;
//                try {
//                    log.info("正在监听消息...");
//                    syncCheckResp = weChatApi.syncCheck();
//                } catch (Exception e) {
//                    log.error("同步检查失败: ", e);
//                    if (retryCount++ < 5) {
//                        log.warn("监听异常，重试第{}次", retryCount);
//                        continue;
//                    } else {
//                        log.error("监听异常，重试次数过多");
//                        return LISTEN_EXCEPTION;
//                    }
//                }
//                retryCount = 0;
//                if (syncCheckResp.getRetCode() > 0) {
//                    log.warn("停止监听信息, 同步检查响应信息: {}", syncCheckResp);
//                    return null;
//                } else if (syncCheckResp.getSelector() > 0) {
//                    WebWxSyncResp webWxSyncResp = weChatApi.webWxSync();
//                    if (webWxSyncResp.getDelContactList() != null) {
//                        // 删除好友立刻触发
//                        // 删除群后的任意一条消息触发
//                        // 被移出群不会触发（会收到一条被移出群的addMsg）
//                        for (User user : webWxSyncResp.getDelContactList()) {
//                            WXContact oldContact = weChatContacts.rmvContact(user.getUserName());
//                            if (oldContact != null && StringUtils.isNotEmpty(oldContact.getName())) {
//                                log.info("删除联系人:{}", user.getUserName());
//                                handleContact(oldContact, null);
//                            }
//                        }
//                    }
//                    if (webWxSyncResp.getModContactList() != null) {
//                        // 添加好友立刻触发
//                        // 被拉入已存在的群立刻触发
//                        // 被拉入新群第一条消息触发（同时收到2条addMsg，一条被拉入群，一条聊天消息）
//                        //  群里有人加入或群里踢人或修改群信息之后第一条信息触发
//                        for (User user : webWxSyncResp.getModContactList()) {
//                            //由于在这里获取到的联系人（无论是群还是用户）的信息是不全的，所以使用接口重新获取
//                            WXContact oldContact = weChatContacts.getContact(user.getUserName());
//                            if (oldContact != null && StringUtils.isEmpty(oldContact.getName())) {
//                                weChatContacts.rmvContact(user.getUserName());
//                                oldContact = null;
//                            }
//                            WXContact newContact = fetchContact(user.getUserName());
//                            if (newContact != null && StringUtils.isEmpty(newContact.getName())) {
//                                weChatContacts.rmvContact(user.getUserName());
//                                newContact = null;
//                            }
//                            if (oldContact != null || newContact != null) {
//                                log.info("变更联系人: {}", user.getUserName());
//                                handleContact(oldContact, newContact);
//                            }
//                        }
//                    }
//                    if (webWxSyncResp.getAddMsgList() != null) {
//                        for (AddMsg addMsg : webWxSyncResp.getAddMsgList()) {
//                            // 接收到的消息，文字、图片、语音、地理位置等等
//                            WXMessage wxMessage = parseMessage(addMsg);
//                            log.info("收到消息:{}", wxMessage);
//                            if (wxMessage instanceof WXNotify wxNotify
//                                    && (wxNotify.getNotifyCode() == WXNotify.NOTIFY_SYNC_CONV)) {
//                                // 会话同步，网页微信仅仅只获取了相关联系人详情
//                                loadContacts(wxNotify.getNotifyContact(), false);
//                            }
//                            // 最后交给监听器处理
//                            handleMessage(wxMessage);
//                        }
//                    }
//                }
//            }
//            TimeUnit.SECONDS.sleep(1);
//            return null;
//        } catch (Exception e) {
//            log.error("监听消息异常: ", e);
//            Thread.currentThread().interrupt();
//            return LISTEN_EXCEPTION;
//        }
//    }
//
//    @Nonnull
//    private <T extends WXMessage> T parseCommon(@Nonnull AddMsg msg, @Nonnull T message) {
//        message.setId(msg.getMsgId());
//        message.setIdLocal(msg.getMsgId());
//        message.setTimestamp(msg.getCreateTime() * 1000);
//        if (msg.getFromUserName().startsWith("@@")) {
//            // 是群消息
//            message.setFromGroup((WXGroup) weChatContacts.getContact(msg.getFromUserName()));
//            if (message.getFromGroup() == null
//                    || !message.getFromGroup().isDetail()
//                    || message.getFromGroup().getMembers().isEmpty()) {
//                // 如果群不存在，或者是未获取成员的群。获取并保存群的详细信息
//                message.setFromGroup((WXGroup) fetchContact(msg.getFromUserName()));
//            }
//            Matcher mGroupMsg = REX_GROUPMSG.matcher(msg.getContent());
//            if (mGroupMsg.matches()) {
//                // 是群成员发送的消息
//                message.setFromUser((WXUser) weChatContacts.getContact(mGroupMsg.group(1)));
//                if (message.getFromUser() == null) {
//                    // 未获取成员。首先获取并保存群的详细信息，然后获取群成员信息
//                    fetchContact(msg.getFromUserName());
//                    message.setFromUser((WXUser) weChatContacts.getContact(mGroupMsg.group(1)));
//                }
//                message.setToContact(weChatContacts.getContact(msg.getToUserName()));
//                if (message.getToContact() == null) {
//                    message.setToContact(fetchContact(msg.getToUserName()));
//                }
//                message.setContent(mGroupMsg.group(2));
//            } else {
//                // 不是群成员发送的消息
//                message.setFromUser(null);
//                message.setToContact(weChatContacts.getContact(msg.getToUserName()));
//                if (message.getToContact() == null) {
//                    message.setToContact(fetchContact(msg.getToUserName()));
//                }
//                message.setContent(msg.getContent());
//            }
//        } else {
//            // 不是群消息
//            message.setFromGroup(null);
//            message.setFromUser((WXUser) weChatContacts.getContact(msg.getFromUserName()));
//            if (message.getFromUser() == null) {
//                // 联系人不存在（一般不会出现这种情况），手动获取联系人
//                message.setFromUser((WXUser) fetchContact(msg.getFromUserName()));
//            }
//            message.setToContact(weChatContacts.getContact(msg.getToUserName()));
//            if (message.getToContact() == null) {
//                message.setToContact(fetchContact(msg.getToUserName()));
//            }
//            message.setContent(msg.getContent());
//        }
//        return message;
//    }
//
//    @Nonnull
//    private WXMessage parseMessage(@Nonnull AddMsg msg) {
//        try {
//            switch (msg.getMsgType()) {
//                case MsgType.TYPE_TEXT: {
//                    if (msg.getSubMsgType() == 0) {
//                        return parseCommon(msg, new WXText());
//                    } else if (msg.getSubMsgType() == MsgType.TYPE_LOCATION) {
//                        WXLocation wxLocation = parseCommon(msg, new WXLocation());
//                        wxLocation.setLocationName(wxLocation.getContent().substring(0, wxLocation.getContent().indexOf(':')));
//                        wxLocation.setLocationImage(String.format("https://%s%s", weChatApi.getHost(),
//                                wxLocation.getContent().substring(wxLocation.getContent().indexOf(':') + ":<br/>".length())));
//                        wxLocation.setLocationUrl(msg.getUrl());
//                        wxLocation.setOriContent(XmlUtil.xmlStrToObject(msg.getOriContent(), OriContent.class));
//                        return wxLocation;
//                    }
//                    break;
//                }
//                case MsgType.TYPE_IMAGE: {
//                    WXImage wxImage = parseCommon(msg, new WXImage());
//                    wxImage.setImgWidth(msg.getImgWidth());
//                    wxImage.setImgHeight(msg.getImgHeight());
//                    wxImage.setImage(weChatApi.webWxGetMsgImg(msg.getMsgId(), "slave"));
//                    return wxImage;
//                }
//                case MsgType.TYPE_VOICE: {
//                    WXVoice wxVoice = parseCommon(msg, new WXVoice());
//                    wxVoice.setVoiceLength(msg.getVoiceLength());
//                    return wxVoice;
//                }
//                case MsgType.TYPE_VERIFY: {
//                    WXVerify wxVerify = parseCommon(msg, new WXVerify());
//                    wxVerify.setUserId(msg.getRecommendInfo().getUserName());
//                    wxVerify.setUserName(msg.getRecommendInfo().getNickName());
//                    wxVerify.setSignature(msg.getRecommendInfo().getSignature());
//                    wxVerify.setProvince(msg.getRecommendInfo().getProvince());
//                    wxVerify.setCity(msg.getRecommendInfo().getCity());
//                    wxVerify.setGender(msg.getRecommendInfo().getSex());
//                    wxVerify.setVerifyFlag(msg.getRecommendInfo().getVerifyFlag());
//                    wxVerify.setTicket(msg.getRecommendInfo().getTicket());
//                    return wxVerify;
//                }
//                case MsgType.TYPE_RECOMMEND: {
//                    WXRecommend wxRecommend = parseCommon(msg, new WXRecommend());
//                    wxRecommend.setUserId(msg.getRecommendInfo().getUserName());
//                    wxRecommend.setUserName(msg.getRecommendInfo().getNickName());
//                    wxRecommend.setGender(msg.getRecommendInfo().getSex());
//                    wxRecommend.setSignature(msg.getRecommendInfo().getSignature());
//                    wxRecommend.setProvince(msg.getRecommendInfo().getProvince());
//                    wxRecommend.setCity(msg.getRecommendInfo().getCity());
//                    wxRecommend.setVerifyFlag(msg.getRecommendInfo().getVerifyFlag());
//                    return wxRecommend;
//                }
//                case MsgType.TYPE_VIDEO: {
//                    // 视频貌似可以分片，后期测试
//                    WXVideo wxVideo = parseCommon(msg, new WXVideo());
//                    wxVideo.setImgWidth(msg.getImgWidth());
//                    wxVideo.setImgHeight(msg.getImgHeight());
//                    wxVideo.setVideoLength(msg.getPlayLength());
//                    wxVideo.setImage(weChatApi.webWxGetMsgImg(msg.getMsgId(), "slave"));
//                    return wxVideo;
//                }
//                case MsgType.TYPE_EMOJI: {
//                    if (StringUtils.isEmpty(msg.getContent()) || msg.getHasProductId() > 0) {
//                        //表情商店的表情，无法下载图片
//                        WXEmoji wxEmoji = parseCommon(msg, new WXEmoji());
//                        wxEmoji.setImgWidth(msg.getImgWidth());
//                        wxEmoji.setImgHeight(msg.getImgHeight());
//                        return wxEmoji;
//                    } else {
//                        //非表情商店的表情，下载图片
//                        WXImage wxImage = parseCommon(msg, new WXImage());
//                        wxImage.setImgWidth(msg.getImgWidth());
//                        wxImage.setImgHeight(msg.getImgHeight());
//                        wxImage.setImage(weChatApi.webWxGetMsgImg(msg.getMsgId(), "big"));
//                        wxImage.setOrigin(wxImage.getImage());
//                        return wxImage;
//                    }
//                }
//                case MsgType.TYPE_OTHER: {
//                    if (msg.getAppMsgType() == 2) {
//                        WXImage wxImage = parseCommon(msg, new WXImage());
//                        wxImage.setImgWidth(msg.getImgWidth());
//                        wxImage.setImgHeight(msg.getImgHeight());
//                        wxImage.setImage(weChatApi.webWxGetMsgImg(msg.getMsgId(), "big"));
//                        wxImage.setOrigin(wxImage.getImage());
//                        return wxImage;
//                    } else if (msg.getAppMsgType() == 5) {
//                        WXLink wxLink = parseCommon(msg, new WXLink());
//                        wxLink.setLinkName(msg.getFileName());
//                        wxLink.setLinkUrl(msg.getUrl());
//                        return wxLink;
//                    } else if (msg.getAppMsgType() == 6) {
//                        WXFile wxFile = parseCommon(msg, new WXFile());
//                        wxFile.setFileId(msg.getMediaId());
//                        wxFile.setFileName(msg.getFileName());
//                        wxFile.setFileSize(StringUtils.isEmpty(msg.getFileSize()) ? 0 : Long.parseLong(msg.getFileSize()));
//                        return wxFile;
//                    } else if (msg.getAppMsgType() == 8) {
//                        WXImage wxImage = parseCommon(msg, new WXImage());
//                        wxImage.setImgWidth(msg.getImgWidth());
//                        wxImage.setImgHeight(msg.getImgHeight());
//                        wxImage.setImage(weChatApi.webWxGetMsgImg(msg.getMsgId(), "big"));
//                        wxImage.setOrigin(wxImage.getImage());
//                        return wxImage;
//                    } else if (msg.getAppMsgType() == 2000) {
//                        return parseCommon(msg, new WXMoney());
//                    }
//                    break;
//                }
//                case MsgType.TYPE_NOTIFY: {
//                    WXNotify wxNotify = parseCommon(msg, new WXNotify());
//                    wxNotify.setNotifyCode(msg.getStatusNotifyCode());
//                    wxNotify.setNotifyContact(msg.getStatusNotifyUserName());
//                    return wxNotify;
//                }
//                case MsgType.TYPE_SYSTEM: {
//                    return parseCommon(msg, new WXSystem());
//                }
//                case MsgType.TYPE_REVOKE:
//                    WXRevoke wxRevoke = parseCommon(msg, new WXRevoke());
//                    Matcher idMatcher = REX_REVOKE_ID.matcher(wxRevoke.getContent());
//                    if (idMatcher.find()) {
//                        wxRevoke.setMsgId(Long.parseLong(idMatcher.group(1)));
//                    }
//                    Matcher replaceMatcher = REX_REVOKE_REPLACE.matcher(wxRevoke.getContent());
//                    if (replaceMatcher.find()) {
//                        wxRevoke.setMsgReplace(replaceMatcher.group(1));
//                    }
//                    return wxRevoke;
//                default:
//                    break;
//            }
//        } catch (Exception e) {
//            log.error("消息解析失败", e);
//        }
//        return parseCommon(msg, new WXUnknown());
//    }
//}