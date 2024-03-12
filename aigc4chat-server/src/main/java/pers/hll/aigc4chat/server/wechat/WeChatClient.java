package pers.hll.aigc4chat.server.wechat;

import lombok.extern.slf4j.Slf4j;
import pers.hll.aigc4chat.common.base.XTools;
import pers.hll.aigc4chat.common.base.http.XHttpTools;
import pers.hll.aigc4chat.common.base.http.executor.impl.XRequest;
import pers.hll.aigc4chat.common.entity.wechat.contact.WXContact;
import pers.hll.aigc4chat.common.entity.wechat.contact.WXGroup;
import pers.hll.aigc4chat.common.entity.wechat.contact.WXUser;
import pers.hll.aigc4chat.common.entity.wechat.message.*;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.request.ReqBatchGetContact;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.request.ReqOplog;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.request.ReqSendMsg;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.response.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.net.HttpCookie;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 模拟网页微信客户端
 *
 * @author hll
 * @since 2023/03/10
 */
@Slf4j
public final class WeChatClient {

    public static final String CFG_PREFIX = "me.xuxiaoxiao$chatapi-wechat$";

    public static final String LOG_TAG = "chatapi-wechat";

    public static final String LOGIN_TIMEOUT = "登陆超时";

    public static final String LOGIN_EXCEPTION = "登陆异常";

    public static final String INIT_EXCEPTION = "初始化异常";

    public static final String LISTEN_EXCEPTION = "监听异常";

    public static final int STATUS_EXCEPTION = -1;

    public static final int STATUS_READY = 0;

    public static final int STATUS_SCAN = 1;

    public static final int STATUS_PERMIT = 2;

    public static final int STATUS_WORKING = 3;

    public static final int STATUS_LOGOUT = 4;

    private static final Pattern REX_GROUPMSG = Pattern.compile("(@[0-9a-zA-Z]+):<br/>([\\s\\S]*)");

    private static final Pattern REX_REVOKE_ID = Pattern.compile("&lt;msgid&gt;(\\d+)&lt;/msgid&gt;");

    private static final Pattern REX_REVOKE_REPLACE =
            Pattern.compile("&lt;replacemsg&gt;&lt;!\\[CDATA\\[([\\s\\S]*)]]&gt;&lt;/replacemsg&gt;");

    private final WeChatThread wxThread = new WeChatThread();

    private final WeChatContacts wxContacts = new WeChatContacts();

    private final WeChatApi wxAPI = new WeChatApi();

    private volatile WeChatListener wxListener;

    private volatile int status = STATUS_READY;

    /**
     * 处理监听器，二维码事件
     *
     * @param qrcode 二维码地址
     */
    private void handleQRCode(@Nonnull String qrcode) {
        this.status = STATUS_SCAN;
        WeChatListener listener = this.wxListener;
        if (listener != null) {
            listener.onQRCode(this, qrcode);
        }
    }

    /**
     * 处理监听器，头像事件
     *
     * @param base64Avatar base64编码头像
     */
    private void handleAvatar(@Nonnull String base64Avatar) {
        this.status = STATUS_PERMIT;
        WeChatListener listener = this.wxListener;
        if (listener != null) {
            listener.onAvatar(this, base64Avatar);
        }
    }

    /**
     * 处理监听器，异常事件
     *
     * @param reason 异常信息
     */
    private void handleFailure(@Nonnull String reason) {
        this.status = STATUS_EXCEPTION;
        WeChatListener listener = this.wxListener;
        if (listener != null) {
            listener.onFailure(this, reason);
        }
    }

    /**
     * 处理监听器，登录完成事件
     */
    private void handleLogin() {
        this.status = STATUS_WORKING;
        WeChatListener listener = this.wxListener;
        if (listener != null) {
            listener.onLogin(this);
        }
    }

    /**
     * 处理监听器，新消息事件
     *
     * @param message 微信消息
     */
    private void handleMessage(WXMessage message) {
        this.status = STATUS_WORKING;
        WeChatListener listener = this.wxListener;
        if (listener != null) {
            listener.onMessage(this, message);
        }
    }

    /**
     * 处理监听器，联系人变动事件
     *
     * @param oldContact 旧联系人，新增联系人时为null
     * @param newContact 新联系人，删除联系人时为null
     */
    private void handleContact(WXContact oldContact, WXContact newContact) {
        this.status = STATUS_WORKING;
        WeChatListener listener = this.wxListener;
        if (listener != null) {
            listener.onContact(this, oldContact, newContact);
        }
    }

    /**
     * 处理监听器，退出登录事件
     */
    private void handleLogout() {
        this.status = STATUS_LOGOUT;
        WeChatListener listener = this.wxListener;
        if (listener != null) {
            listener.onLogout(this);
        }
    }

    /**
     * 获取并保存不限数量和类型的联系人信息
     *
     * @param userNames 逗号分隔的联系人userName
     */
    private void loadContacts(@Nonnull String userNames, boolean useCache) {
        if (!XTools.strEmpty(userNames)) {
            LinkedList<ReqBatchGetContact.Contact> contacts = new LinkedList<>();
            for (String userName : userNames.split(",")) {
                if (!XTools.strEmpty(userName)) {
                    contacts.add(new ReqBatchGetContact.Contact(userName, ""));
                }
            }
            loadContacts(contacts, useCache);
        }
    }

    /**
     * 获取并保存不限数量和类型的联系人信息
     *
     * @param contacts 要获取的联系人的列表，数量和类型不限
     */
    private void loadContacts(@Nonnull List<ReqBatchGetContact.Contact> contacts, boolean useCache) {
        if (useCache) {
            // 不是群聊，并且已经获取过，就不再次获取
            contacts.removeIf(contact -> !contact.userName.startsWith("@@") && wxContacts.getContact(contact.userName) != null);
        }
        // 拆分成每次50个联系人分批获取
        if (contacts.size() > 50) {
            LinkedList<ReqBatchGetContact.Contact> temp = new LinkedList<>();
            for (ReqBatchGetContact.Contact contact : contacts) {
                temp.add(contact);
                if (temp.size() >= 50) {
                    RspBatchGetContact rspBatchGetContact = wxAPI.webwxbatchgetcontact(contacts);
                    for (RspInit.User user : rspBatchGetContact.ContactList) {
                        wxContacts.putContact(wxAPI.host, user);
                    }
                    temp.clear();
                }
            }
            contacts = temp;
        }
        if (!contacts.isEmpty()) {
            RspBatchGetContact rspBatchGetContact = wxAPI.webwxbatchgetcontact(contacts);
            for (RspInit.User user : rspBatchGetContact.ContactList) {
                wxContacts.putContact(wxAPI.host, user);
            }
        }
    }

    /**
     * 打印Cookie和登录信息
     */
    public void dump() {
        try {
            XTools.logE(LOG_TAG, "微信用户：" + userMe().getName());

            StringBuilder sbCookie = new StringBuilder("Cookie信息：");
            //Field created = HttpCookie.class.getDeclaredField("whenCreated");
            //created.setAccessible(true);
            for (HttpCookie cookie : wxAPI.getHttpExecutor().getCookies()) {
                //sbCookie.append("\n\t过期时间：")
                //        .append(XTools.dateFormat(XTimeTools.FORMAT_YMDHMS, new Date((long) created.get(cookie) + cookie.getMaxAge() * 1000)));
                sbCookie.append(", 键: ").append(cookie.getName());
                sbCookie.append(", 值: ").append(cookie.getValue());
            }
            XTools.logE(LOG_TAG, sbCookie.toString());

            StringBuilder sbLogin = new StringBuilder("登录信息：");
            sbLogin.append("\n\thost：").append(wxAPI.host);
            sbLogin.append("\n\tuin：").append(wxAPI.uin);
            sbLogin.append("\n\tsid：").append(wxAPI.sid);
            sbLogin.append("\n\tdataTicket：").append(wxAPI.dataTicket);
            sbLogin.append("\n\tuuid：").append(wxAPI.getUuid());
            sbLogin.append("\n\tskey：").append(wxAPI.getSkey());
            sbLogin.append("\n\tpassticket：").append(wxAPI.getPassticket());
            sbLogin.append("\n\tsynckey：").append(wxAPI.getSynckey());
            sbLogin.append("\n\tsyncCheckKey：").append(wxAPI.getSyncCheckKey());
            XTools.logE(LOG_TAG, sbLogin.toString().replace("%", "%%"));
        } catch (Exception e) {
            log.error("", e);
        }
    }

    /**
     * 设置客户端的监听器
     *
     * @param listener 监听器对象
     */
    public void setListener(@Nonnull WeChatListener listener) {
        this.wxListener = listener;
    }

    /**
     * 获取客户端的监听器
     *
     * @return 监听器对象
     */
    @Nullable
    public WeChatListener getListener() {
        return this.wxListener;
    }

    /**
     * 启动客户端，注意：一个客户端类的实例只能被启动一次
     */
    public void startup() {
        wxThread.start();
    }

    /**
     * 获取客户端的状态
     *
     * @return 客户端的当前状态
     */
    public int status() {
        return this.status;
    }

    /**
     * 关闭客户端，注意：关闭后的客户端不能再被启动
     */
    public void shutdown() {
        wxAPI.webwxlogout();
        wxThread.interrupt();
    }

    /**
     * 获取当前登录的用户信息
     *
     * @return 当前登录的用户信息
     */
    public WXUser userMe() {
        return wxContacts.getMe();
    }

    /**
     * 根据userId获取用户好友
     *
     * @param userId 好友的id
     * @return 好友的信息
     */
    @Nullable
    public WXUser userFriend(@Nonnull String userId) {
        return wxContacts.getFriend(userId);
    }

    /**
     * 获取用户所有好友
     *
     * @return 用户所有好友
     */
    @Nonnull
    public Map<String, WXUser> userFriends() {
        return wxContacts.getFriends();
    }

    /**
     * 根据群id获取群信息
     *
     * @param groupId 群id
     * @return 群信息
     */
    @Nullable
    public WXGroup userGroup(@Nonnull String groupId) {
        return wxContacts.getGroup(groupId);
    }

    /**
     * 获取用户所有群
     *
     * @return 用户所有群
     */
    @Nonnull
    public Map<String, WXGroup> userGroups() {
        return wxContacts.getGroups();
    }

    /**
     * 根据联系人id获取用户联系人信息
     *
     * @param contactId 联系人id
     * @return 联系人信息
     */
    @Nullable
    public WXContact userContact(@Nonnull String contactId) {
        return wxContacts.getContact(contactId);
    }

    /**
     * 发送文字消息
     *
     * @param wxContact 目标联系人
     * @param text      要发送的文字
     * @return 文本消息
     */
    @Nonnull
    public WXText sendText(@Nonnull WXContact wxContact, @Nonnull String text) {
        XTools.logN(LOG_TAG, String.format("向（%s：%s）发送文字消息：%s", wxContact.getId(), wxContact.getName(), text));
        RspSendMsg rspSendMsg = wxAPI.webwxsendmsg(new ReqSendMsg.Msg(RspSync.AddMsg.TYPE_TEXT, null, 0,
                text, null, wxContacts.getMe().getId(), wxContact.getId()));

        WXText wxText = new WXText();
        wxText.setId(Long.parseLong(rspSendMsg.MsgID));
        wxText.setIdLocal(Long.parseLong(rspSendMsg.LocalID));
        wxText.setTimestamp(System.currentTimeMillis());
        wxText.setFromGroup(null);
        wxText.setFromUser(wxContacts.getMe());
        wxText.setToContact(wxContact);
        wxText.setContent(text);
        return wxText;
    }

    /**
     * 发送文件消息，可以是图片，动图，视频，文本等文件
     *
     * @param wxContact 目标联系人
     * @param file      要发送的文件
     * @return 图像或附件消息
     */
    @Nullable
    public WXMessage sendFile(@Nonnull WXContact wxContact, @Nonnull File file) {
        String suffix = WeChatTools.fileSuffix(file);
        if ("mp4".equals(suffix) && file.length() >= 20L * 1024L * 1024L) {
            XTools.logW(LOG_TAG, String.format("向（%s：%s）发送的视频文件大于20M，无法发送", wxContact.getId(), wxContact.getName()));
            return null;
        } else {
            try {
                XTools.logN(LOG_TAG, String.format("向（%s：%s）发送文件：%s", wxContact.getId(), wxContact.getName(), file.getAbsolutePath()));
                String mediaId = null, aesKey = null, signature = null;
                if (file.length() >= 25L * 1024L * 1024L) {
                    RspCheckUpload rspCheckUpload = wxAPI.webwxcheckupload(file, wxContacts.getMe().getId(), wxContact.getId());
                    mediaId = rspCheckUpload.MediaId;
                    aesKey = rspCheckUpload.AESKey;
                    signature = rspCheckUpload.Signature;
                }
                if (XTools.strEmpty(mediaId)) {
                    RspUploadMedia rspUploadMedia = wxAPI.webwxuploadmedia(wxContacts.getMe().getId(), wxContact.getId(), file, aesKey, signature);
                    mediaId = rspUploadMedia.MediaId;
                }

                if (!XTools.strEmpty(mediaId)) {
                    switch (WeChatTools.fileType(file)) {
                        case "pic": {
                            RspSendMsg rspSendMsg = wxAPI.webwxsendmsgimg(new ReqSendMsg.Msg(RspSync.AddMsg.TYPE_IMAGE, mediaId, null, "", signature, wxContacts.getMe().getId(), wxContact.getId()));
                            WXImage wxImage = new WXImage();
                            wxImage.setId(Long.parseLong(rspSendMsg.MsgID));
                            wxImage.setIdLocal(Long.parseLong(rspSendMsg.LocalID));
                            wxImage.setTimestamp(System.currentTimeMillis());
                            wxImage.setFromGroup(null);
                            wxImage.setFromUser(wxContacts.getMe());
                            wxImage.setToContact(wxContact);
                            wxImage.setImgWidth(0);
                            wxImage.setImgHeight(0);
                            wxImage.setImage(wxAPI.webwxgetmsgimg(wxImage.getId(), "slave"));
                            wxImage.setOrigin(file);
                            return wxImage;
                        }
                        case "video": {
                            RspSendMsg rspSendMsg = wxAPI.webwxsendvideomsg(new ReqSendMsg.Msg(RspSync.AddMsg.TYPE_VIDEO, mediaId, null, "", signature, wxContacts.getMe().getId(), wxContact.getId()));
                            WXVideo wxVideo = new WXVideo();
                            wxVideo.setId(Long.parseLong(rspSendMsg.MsgID));
                            wxVideo.setIdLocal(Long.parseLong(rspSendMsg.LocalID));
                            wxVideo.setTimestamp(System.currentTimeMillis());
                            wxVideo.setFromGroup(null);
                            wxVideo.setFromUser(wxContacts.getMe());
                            wxVideo.setToContact(wxContact);
                            wxVideo.setImgWidth(0);
                            wxVideo.setImgHeight(0);
                            wxVideo.setImage(wxAPI.webwxgetmsgimg(wxVideo.getId(), "slave"));
                            wxVideo.setVideoLength(0);
                            wxVideo.setVideo(file);
                            return wxVideo;
                        }
                        default:
                            if ("gif".equals(suffix)) {
                                RspSendMsg rspSendMsg = wxAPI.webwxsendemoticon(new ReqSendMsg.Msg(RspSync.AddMsg.TYPE_EMOJI, mediaId, 2, "", signature, wxContacts.getMe().getId(), wxContact.getId()));
                                WXImage wxImage = new WXImage();
                                wxImage.setId(Long.parseLong(rspSendMsg.MsgID));
                                wxImage.setIdLocal(Long.parseLong(rspSendMsg.LocalID));
                                wxImage.setTimestamp(System.currentTimeMillis());
                                wxImage.setFromGroup(null);
                                wxImage.setFromUser(wxContacts.getMe());
                                wxImage.setToContact(wxContact);
                                wxImage.setImgWidth(0);
                                wxImage.setImgHeight(0);
                                wxImage.setImage(file);
                                wxImage.setOrigin(file);
                                return wxImage;
                            } else {
                                StringBuilder sbAppMsg = new StringBuilder();
                                sbAppMsg.append("<appmsg appid='wxeb7ec651dd0aefa9' sdkver=''>");
                                sbAppMsg.append("<title>").append(file.getName()).append("</title>");
                                sbAppMsg.append("<des></des>");
                                sbAppMsg.append("<action></action>");
                                sbAppMsg.append("<type>6</type>");
                                sbAppMsg.append("<content></content>");
                                sbAppMsg.append("<url></url>");
                                sbAppMsg.append("<lowurl></lowurl>");
                                sbAppMsg.append("<appattach>");
                                sbAppMsg.append("<totallen>").append(file.length()).append("</totallen>");
                                sbAppMsg.append("<attachid>").append(mediaId).append("</attachid>");
                                sbAppMsg.append("<fileext>").append(XTools.strEmpty(suffix) ? "undefined" : suffix).append("</fileext>");
                                sbAppMsg.append("</appattach>");
                                sbAppMsg.append("<extinfo></extinfo>");
                                sbAppMsg.append("</appmsg>");
                                RspSendMsg rspSendMsg = wxAPI.webwxsendappmsg(new ReqSendMsg.Msg(6, null, null, sbAppMsg.toString(), signature, wxContacts.getMe().getId(), wxContact.getId()));
                                WXFile wxFile = new WXFile();
                                wxFile.setId(Long.parseLong(rspSendMsg.MsgID));
                                wxFile.setIdLocal(Long.parseLong(rspSendMsg.LocalID));
                                wxFile.setTimestamp(System.currentTimeMillis());
                                wxFile.setFromGroup(null);
                                wxFile.setFromUser(wxContacts.getMe());
                                wxFile.setToContact(wxContact);
                                wxFile.setContent(sbAppMsg.toString());
                                wxFile.setFileSize(file.length());
                                wxFile.setFileName(file.getName());
                                wxFile.setFileId(mediaId);
                                wxFile.setFile(file);
                                return wxFile;
                            }
                    }
                } else {
                    XTools.logE(LOG_TAG, String.format("向（%s：%s）发送的文件发送失败", wxContact.getId(), wxContact.getName()));
                }
            } catch (IOException e) {
                log.error("", e);
            }
        }
        return null;
    }

    /**
     * 发送位置消息
     * <p>
     * 经纬度坐标可以通过腾讯坐标拾取工具获得(<a href="https://lbs.qq.com/tool/getpoint">腾讯坐标工具</a>)
     * 其拾取的坐标默认格式为 lat,lon
     * </p>
     *
     * @param wxContact 目标联系人
     * @param lon       经度
     * @param lat       纬度
     * @param title     定位消息模块标题
     * @param lable     定位消息模块二级描述
     * @return 定位消息
     */
    @Nonnull
    public WXLocation sendLocation(@Nonnull WXContact wxContact, @Nonnull String lon, @Nonnull String lat,
                                   @Nonnull String title, @Nonnull String lable) {
        XTools.logN(LOG_TAG, String.format("向（%s：%s）发送位置信息，坐标：%s,%s，说明：%s(%s)",
                wxContact.getId(), wxContact.getName(), lon, lat, title, lable));
        StringBuilder sbLocationMsg = new StringBuilder();
        sbLocationMsg.append("<?xml version=\"1.0\"?>\n");
        sbLocationMsg.append("<msg>\n");
        sbLocationMsg.append("<location x=\"")
                .append(lat)
                .append("\" y=\"")
                .append(lon)
                .append("\" scale=\"15\" label=\"")
                .append(lable)
                .append("\" maptype=\"roadmap\" poiname=\"")
                .append(title)
                .append("\" poiid=\"City\" />\n");
        sbLocationMsg.append("</msg>\n");
        RspSendMsg rspSendMsg = wxAPI.webwxsendmsg(
                new ReqSendMsg.Msg(RspSync.AddMsg.TYPE_LOCATION, null, 0, sbLocationMsg.toString(),
                        null, wxContacts.getMe().getId(), wxContact.getId()));
        WXLocation wxLocation = new WXLocation();
        wxLocation.setId(Long.parseLong(rspSendMsg.MsgID));
        wxLocation.setIdLocal(Long.parseLong(rspSendMsg.LocalID));
        wxLocation.setTimestamp(System.currentTimeMillis());
        wxLocation.setFromGroup(null);
        wxLocation.setFromUser(wxContacts.getMe());
        wxLocation.setToContact(wxContact);
        wxLocation.setContent(sbLocationMsg.toString());
        return wxLocation;
    }

    /**
     * 获取用户联系人，如果获取的联系人是群组，则会自动获取群成员的详细信息
     * <strong>在联系人列表中获取到的群，没有群成员，可以通过这个方法，获取群的详细信息</strong>
     *
     * @param contactId 联系人id
     * @return 联系人的详细信息
     */
    @Nullable
    public WXContact fetchContact(@Nonnull String contactId) {
        loadContacts(contactId, false);
        WXContact contact = wxContacts.getContact(contactId);
        if (contact instanceof WXGroup) {
            List<ReqBatchGetContact.Contact> contacts = new LinkedList<>();
            for (WXGroup.Member member : ((WXGroup) contact).getMembers().values()) {
                contacts.add(new ReqBatchGetContact.Contact(member.getId(), contact.getId()));
            }
            loadContacts(contacts, true);
            ((WXGroup) contact).setDetail(true);
        }
        return contact;
    }

    /**
     * 获取用户头像
     *
     * @param wxContact 要获取头像文件的用户
     * @return 获取头像文件后的用户
     */
    @Nonnull
    public WXContact fetchAvatar(@Nonnull WXContact wxContact) {
        wxContact.setAvatarFile(XTools
                .http(XHttpTools.EXECUTOR, XRequest.GET(wxContact.getAvatarUrl()))
                .file(wxAPI.folder.getAbsolutePath()
                        + File.separator
                        + String.format("avatar-%d.jpg", System.currentTimeMillis()
                        + new Random().nextInt(1000))));
        return wxContact;
    }

    /**
     * 获取图片消息的大图
     *
     * @param wxImage 要获取大图的图片消息
     * @return 获取大图后的图片消息
     */
    @Nonnull
    public WXImage fetchImage(@Nonnull WXImage wxImage) {
        wxImage.setOrigin(wxAPI.webwxgetmsgimg(wxImage.getId(), "big"));
        return wxImage;
    }

    /**
     * 获取语音消息的语音文件
     *
     * @param wxVoice 语音消息
     * @return 获取语音文件后的语音消息
     */
    @Nonnull
    public WXVoice fetchVoice(@Nonnull WXVoice wxVoice) {
        wxVoice.setVoice(wxAPI.webwxgetvoice(wxVoice.getId()));
        return wxVoice;
    }

    /**
     * 获取视频消息的视频文件
     *
     * @param wxVideo 视频消息
     * @return 获取视频文件后的视频消息
     */
    @Nonnull
    public WXVideo fetchVideo(@Nonnull WXVideo wxVideo) {
        wxVideo.setVideo(wxAPI.webwxgetvideo(wxVideo.getId()));
        return wxVideo;
    }

    /**
     * 获取文件消息的附件文件
     *
     * @param wxFile 文件消息
     * @return 获取附件文件后的文件消息
     */
    @Nonnull
    public WXFile fetchFile(@Nonnull WXFile wxFile) {
        wxFile.setFile(wxAPI.webwxgetmedia(
                wxFile.getId(), wxFile.getFileName(), wxFile.getFileId(), wxFile.getFromUser().getId()));
        return wxFile;
    }

    /**
     * 撤回消息
     *
     * @param wxMessage 需要撤回的微信消息
     */
    public void revokeMsg(@Nonnull WXMessage wxMessage) {
        XTools.logN(LOG_TAG, String.format("撤回向（%s：%s）发送的消息：%s，%s",
                wxMessage.getToContact().getId(), wxMessage.getToContact().getName(), wxMessage.getIdLocal(), wxMessage.getId()));
        wxAPI.webwxrevokemsg(wxMessage.getIdLocal(), wxMessage.getId(), wxMessage.getToContact().getId());
    }

    /**
     * 同意好友申请
     *
     * @param wxVerify 好友验证消息
     */
    public void passVerify(@Nonnull WXVerify wxVerify) {
        XTools.logN(LOG_TAG, String.format("通过（%s：%s）的好友申请", wxVerify.getUserId(), wxVerify.getUserName()));
        wxAPI.webwxverifyuser(3, wxVerify.getUserId(), wxVerify.getTicket(), "");
    }

    /**
     * 修改用户备注名
     *
     * @param wxUser 目标用户
     * @param remark 备注名称
     */
    public void editRemark(@Nonnull WXUser wxUser, @Nonnull String remark) {
        XTools.logN(LOG_TAG, String.format("修改（%s：%s）的备注：%s", wxUser.getId(), wxUser.getName(), remark));
        wxAPI.webwxoplog(ReqOplog.CMD_REMARK, ReqOplog.OP_NONE, wxUser.getId(), remark);
    }

    /**
     * 设置联系人置顶状态
     *
     * @param wxContact 需要设置置顶状态的联系人
     * @param isTop     是否置顶
     */
    public void topContact(@Nonnull WXContact wxContact, boolean isTop) {
        XTools.logN(LOG_TAG, String.format("设置（%s：%s）的置顶状态：%s", wxContact.getId(), wxContact.getName(), isTop));
        wxAPI.webwxoplog(ReqOplog.CMD_TOP, isTop ? ReqOplog.OP_TOP_TRUE : ReqOplog.OP_TOP_FALSE, wxContact.getId(), null);
    }

    /**
     * 修改聊天室名称
     *
     * @param wxGroup 目标聊天室
     * @param name    目标名称
     */
    public void setGroupName(@Nonnull WXGroup wxGroup, @Nonnull String name) {
        XTools.logN(LOG_TAG, String.format("将群（%s：%s）的名称修改为：%s", wxGroup.getId(), wxGroup.getName(), name));
        wxAPI.webwxupdatechartroom(wxGroup.getId(), "modtopic", name, new LinkedList<String>());
    }

    /**
     * 模拟网页微信客户端监听器
     */
    public interface WeChatListener {
        /**
         * 获取到用户登录的二维码
         *
         * @param client 微信客户端
         * @param qrCode 用户登录二维码的url
         */
        default void onQRCode(@Nonnull WeChatClient client, @Nonnull String qrCode) {
        }

        /**
         * 获取用户头像，base64编码
         *
         * @param client       微信客户端
         * @param base64Avatar base64编码的用户头像
         */
        default void onAvatar(@Nonnull WeChatClient client, @Nonnull String base64Avatar) {
        }

        /**
         * 模拟网页微信客户端异常退出
         *
         * @param client 微信客户端
         * @param reason 错误原因
         */
        default void onFailure(@Nonnull WeChatClient client, @Nonnull String reason) {
            client.dump();
        }

        /**
         * 用户登录并初始化成功
         *
         * @param client 微信客户端
         */
        default void onLogin(@Nonnull WeChatClient client) {
        }

        /**
         * 用户获取到消息
         *
         * @param client  微信客户端
         * @param message 用户获取到的消息
         */
        default void onMessage(@Nonnull WeChatClient client, @Nonnull WXMessage message) {
        }

        /**
         * 用户联系人变化
         *
         * @param client     微信客户端
         * @param oldContact 旧联系人，新增联系人时为null
         * @param newContact 新联系人，删除联系人时为null
         */
        default void onContact(@Nonnull WeChatClient client, @Nullable WXContact oldContact, @Nullable WXContact newContact) {
        }

        /**
         * 模拟网页微信客户端正常退出
         *
         * @param client 微信客户端
         */
        default void onLogout(@Nonnull WeChatClient client) {
            client.dump();
        }
    }

    /**
     * 模拟网页微信客户端工作线程
     */
    private class WeChatThread extends Thread {

        @Override
        public void run() {
            int loginCount = 0;
            while (!isInterrupted()) {
                // 用户登录
                XTools.logD(LOG_TAG, "正在登录");
                String loginErr = login();
                if (!XTools.strEmpty(loginErr)) {
                    XTools.logE(LOG_TAG, String.format("登录出现错误：%s", loginErr));
                    handleFailure(loginErr);
                }
                // 用户初始化
                XTools.logD(LOG_TAG, "正在初始化");
                String initErr = initial();
                if (!XTools.strEmpty(initErr)) {
                    XTools.logE(LOG_TAG, String.format("初始化出现错误：%s", initErr));
                    handleFailure(initErr);
                    break;
                }
                handleLogin();
                // 同步消息
                XTools.logD(LOG_TAG, "正在监听消息");
                String listenErr = listen();
                if (!XTools.strEmpty(listenErr)) {
                    if (loginCount++ > 10) {
                        handleFailure(listenErr);
                        break;
                    } else {
                        continue;
                    }
                }
                // 退出登录
                XTools.logD(LOG_TAG, "正在退出登录");
                handleLogout();
                break;
            }
        }

        /**
         * 用户登录
         *
         * @return 登录时异常原因，为null表示正常登录
         */
        @Nullable
        private String login() {
            try {
                if (XTools.strEmpty(wxAPI.sid)) {
                    String qrCode = wxAPI.jsLogin();
                    handleQRCode(qrCode);
                    while (true) {
                        RspLogin rspLogin = wxAPI.login();
                        switch (rspLogin.code) {
                            case 200:
                                XTools.logD(LOG_TAG, "已授权登录");
                                wxAPI.webWxNewLoginPage(rspLogin.redirectUri);
                                return null;
                            case 201:
                                XTools.logD(LOG_TAG, "已扫描二维码");
                                handleAvatar(rspLogin.userAvatar);
                                break;
                            case 408:
                                XTools.logD(LOG_TAG, "等待授权登录");
                                break;
                            default:
                                XTools.logW(LOG_TAG, "登录超时");
                                return LOGIN_TIMEOUT;
                        }
                    }
                } else {
                    return null;
                }
            } catch (Exception e) {
                log.error("", e);
                XTools.logW(LOG_TAG, e, "登录异常");
                return LOGIN_EXCEPTION;
            }
        }

        /**
         * 初始化
         *
         * @return 初始化异常原因，为null表示正常初始化
         */
        @Nullable
        private String initial() {
            try {
                // 通过Cookie获取重要参数
                XTools.logD(LOG_TAG, "正在获取Cookie");
                for (HttpCookie cookie : XHttpTools.EXECUTOR.getCookies()) {
                    if ("wxsid".equalsIgnoreCase(cookie.getName())) {
                        wxAPI.sid = cookie.getValue();
                    } else if ("wxuin".equalsIgnoreCase(cookie.getName())) {
                        wxAPI.uin = cookie.getValue();
                    } else if ("webwx_data_ticket".equalsIgnoreCase(cookie.getName())) {
                        wxAPI.dataTicket = cookie.getValue();
                    }
                }

                // 获取自身信息
                XTools.logD(LOG_TAG, "正在获取自身信息");
                RspInit rspInit = wxAPI.webWxInit();
                wxContacts.setMe(wxAPI.host, rspInit.User);

                // 获取并保存最近联系人
                XTools.logD(LOG_TAG, "正在获取并保存最近联系人");
                loadContacts(rspInit.ChatSet, true);

                // 发送初始化状态信息
                wxAPI.webWxStatusNotify(wxContacts.getMe().getId(), WXNotify.NOTIFY_INITED);

                // 获取好友、保存的群聊、公众号列表。
                // 这里获取的群没有群成员，不过也不能用fetchContact方法暴力获取群成员，因为这样数据量会很大
                XTools.logD(LOG_TAG, "正在获取好友、群、公众号列表");
                RspGetContact rspGetContact = wxAPI.webwxgetcontact();
                for (RspInit.User user : rspGetContact.MemberList) {
                    wxContacts.putContact(wxAPI.host, user);
                }
                return null;
            } catch (Exception e) {
                log.error("", e);
                XTools.logW(LOG_TAG, String.format("初始化异常：%s", e.getMessage()));
                return INIT_EXCEPTION;
            }
        }

        /**
         * 循环同步消息
         *
         * @return 同步消息的异常原因，为null表示正常结束
         */
        @Nullable
        private String listen() {
            int retryCount = 0;
            try {
                while (!isInterrupted()) {
                    RspSyncCheck rspSyncCheck;
                    try {
                        XTools.logD(LOG_TAG, "正在监听信息");
                        rspSyncCheck = wxAPI.synccheck();
                    } catch (Exception e) {
                        log.error("", e);
                        if (retryCount++ < 5) {
                            XTools.logW(LOG_TAG, e, String.format("监听异常，重试第%d次", retryCount));
                            continue;
                        } else {
                            XTools.logE(LOG_TAG, e, "监听异常，重试次数过多");
                            return LISTEN_EXCEPTION;
                        }
                    }
                    retryCount = 0;
                    if (rspSyncCheck.retcode > 0) {
                        XTools.logW(LOG_TAG, String.format("停止监听信息：%d", rspSyncCheck.retcode));
                        return null;
                    } else if (rspSyncCheck.selector > 0) {
                        RspSync rspSync = wxAPI.webwxsync();
                        if (rspSync.DelContactList != null) {
                            // 删除好友立刻触发
                            // 删除群后的任意一条消息触发
                            // 被移出群不会触发（会收到一条被移出群的addMsg）
                            for (RspInit.User user : rspSync.DelContactList) {
                                WXContact oldContact = wxContacts.rmvContact(user.UserName);
                                if (oldContact != null && !XTools.strEmpty(oldContact.getName())) {
                                    XTools.logN(LOG_TAG, String.format("删除联系人（%s）", user.UserName));
                                    handleContact(oldContact, null);
                                }
                            }
                        }
                        if (rspSync.ModContactList != null) {
                            // 添加好友立刻触发
                            // 被拉入已存在的群立刻触发
                            // 被拉入新群第一条消息触发（同时收到2条addMsg，一条被拉入群，一条聊天消息）
                            //  群里有人加入或群里踢人或修改群信息之后第一条信息触发
                            for (RspInit.User user : rspSync.ModContactList) {
                                //由于在这里获取到的联系人（无论是群还是用户）的信息是不全的，所以使用接口重新获取
                                WXContact oldContact = wxContacts.getContact(user.UserName);
                                if (oldContact != null && XTools.strEmpty(oldContact.getName())) {
                                    wxContacts.rmvContact(user.UserName);
                                    oldContact = null;
                                }
                                WXContact newContact = fetchContact(user.UserName);
                                if (newContact != null && XTools.strEmpty(newContact.getName())) {
                                    wxContacts.rmvContact(user.UserName);
                                    newContact = null;
                                }
                                if (oldContact != null || newContact != null) {
                                    XTools.logN(LOG_TAG, String.format("变更联系人（%s）", user.UserName));
                                    handleContact(oldContact, newContact);
                                }
                            }
                        }
                        if (rspSync.AddMsgList != null) {
                            for (RspSync.AddMsg addMsg : rspSync.AddMsgList) {
                                // 接收到的消息，文字、图片、语音、地理位置等等
                                WXMessage wxMessage = parseMessage(addMsg);
                                if (wxMessage instanceof WXNotify wxNotify
                                        && (wxNotify.getNotifyCode() == WXNotify.NOTIFY_SYNC_CONV)) {
                                    // 会话同步，网页微信仅仅只获取了相关联系人详情
                                    loadContacts(wxNotify.getNotifyContact(), false);
                                }
                                // 最后交给监听器处理
                                handleMessage(wxMessage);
                            }
                        }
                    }
                }
                return null;
            } catch (Exception e) {
                log.error("", e);
                XTools.logW(LOG_TAG, e, "监听消息异常");
                return LISTEN_EXCEPTION;
            }
        }

        @Nonnull
        private <T extends WXMessage> T parseCommon(@Nonnull RspSync.AddMsg msg, @Nonnull T message) {
            message.setId(msg.MsgId);
            message.setIdLocal(msg.MsgId);
            message.setTimestamp(msg.CreateTime * 1000);
            if (msg.FromUserName.startsWith("@@")) {
                // 是群消息
                message.setFromGroup((WXGroup) wxContacts.getContact(msg.FromUserName));
                if (message.getFromGroup() == null
                        || !message.getFromGroup().isDetail()
                        || message.getFromGroup().getMembers().isEmpty()) {
                    // 如果群不存在，或者是未获取成员的群。获取并保存群的详细信息
                    message.setFromGroup((WXGroup) fetchContact(msg.FromUserName));
                }
                Matcher mGroupMsg = REX_GROUPMSG.matcher(msg.Content);
                if (mGroupMsg.matches()) {
                    // 是群成员发送的消息
                    message.setFromUser((WXUser) wxContacts.getContact(mGroupMsg.group(1)));
                    if (message.getFromUser() == null) {
                        // 未获取成员。首先获取并保存群的详细信息，然后获取群成员信息
                        fetchContact(msg.FromUserName);
                        message.setFromUser((WXUser) wxContacts.getContact(mGroupMsg.group(1)));
                    }
                    message.setToContact(wxContacts.getContact(msg.ToUserName));
                    if (message.getToContact() == null) {
                        message.setToContact(fetchContact(msg.ToUserName));
                    }
                    message.setContent(mGroupMsg.group(2));
                } else {
                    // 不是群成员发送的消息
                    message.setFromUser(null);
                    message.setToContact(wxContacts.getContact(msg.ToUserName));
                    if (message.getToContact() == null) {
                        message.setToContact(fetchContact(msg.ToUserName));
                    }
                    message.setContent(msg.Content);
                }
            } else {
                // 不是群消息
                message.setFromGroup(null);
                message.setFromUser((WXUser) wxContacts.getContact(msg.FromUserName));
                if (message.getFromUser() == null) {
                    // 联系人不存在（一般不会出现这种情况），手动获取联系人
                    message.setFromUser((WXUser) fetchContact(msg.FromUserName));
                }
                message.setToContact(wxContacts.getContact(msg.ToUserName));
                if (message.getToContact() == null) {
                    message.setToContact(fetchContact(msg.ToUserName));
                }
                message.setContent(msg.Content);
            }
            return message;
        }

        @Nonnull
        private WXMessage parseMessage(@Nonnull RspSync.AddMsg msg) {
            try {
                switch (msg.MsgType) {
                    case RspSync.AddMsg.TYPE_TEXT: {
                        if (msg.SubMsgType == 0) {
                            return parseCommon(msg, new WXText());
                        } else if (msg.SubMsgType == 48) {
                            WXLocation wxLocation = parseCommon(msg, new WXLocation());
                            wxLocation.setLocationName(wxLocation.getContent().substring(0, wxLocation.getContent().indexOf(':')));
                            wxLocation.setLocationImage(String.format("https://%s%s", wxAPI.host,
                                    wxLocation.getContent().substring(wxLocation.getContent().indexOf(':') + ":<br/>".length())));
                            wxLocation.setLocationUrl(msg.Url);
                            return wxLocation;
                        }
                        break;
                    }
                    case RspSync.AddMsg.TYPE_IMAGE: {
                        WXImage wxImage = parseCommon(msg, new WXImage());
                        wxImage.setImgWidth(msg.ImgWidth);
                        wxImage.setImgHeight(msg.ImgHeight);
                        wxImage.setImage(wxAPI.webwxgetmsgimg(msg.MsgId, "slave"));
                        return wxImage;
                    }
                    case RspSync.AddMsg.TYPE_VOICE: {
                        WXVoice wxVoice = parseCommon(msg, new WXVoice());
                        wxVoice.setVoiceLength(msg.VoiceLength);
                        return wxVoice;
                    }
                    case RspSync.AddMsg.TYPE_VERIFY: {
                        WXVerify wxVerify = parseCommon(msg, new WXVerify());
                        wxVerify.setUserId(msg.RecommendInfo.UserName);
                        wxVerify.setUserName(msg.RecommendInfo.NickName);
                        wxVerify.setSignature(msg.RecommendInfo.Signature);
                        wxVerify.setProvince(msg.RecommendInfo.Province);
                        wxVerify.setCity(msg.RecommendInfo.City);
                        wxVerify.setGender(msg.RecommendInfo.Sex);
                        wxVerify.setVerifyFlag(msg.RecommendInfo.VerifyFlag);
                        wxVerify.setTicket(msg.RecommendInfo.Ticket);
                        return wxVerify;
                    }
                    case RspSync.AddMsg.TYPE_RECOMMEND: {
                        WXRecommend wxRecommend = parseCommon(msg, new WXRecommend());
                        wxRecommend.setUserId(msg.RecommendInfo.UserName);
                        wxRecommend.setUserName(msg.RecommendInfo.NickName);
                        wxRecommend.setGender(msg.RecommendInfo.Sex);
                        wxRecommend.setSignature(msg.RecommendInfo.Signature);
                        wxRecommend.setProvince(msg.RecommendInfo.Province);
                        wxRecommend.setCity(msg.RecommendInfo.City);
                        wxRecommend.setVerifyFlag(msg.RecommendInfo.VerifyFlag);
                        return wxRecommend;
                    }
                    case RspSync.AddMsg.TYPE_VIDEO: {
                        //视频貌似可以分片，后期测试
                        WXVideo wxVideo = parseCommon(msg, new WXVideo());
                        wxVideo.setImgWidth(msg.ImgWidth);
                        wxVideo.setImgHeight(msg.ImgHeight);
                        wxVideo.setVideoLength(msg.PlayLength);
                        wxVideo.setImage(wxAPI.webwxgetmsgimg(msg.MsgId, "slave"));
                        return wxVideo;
                    }
                    case RspSync.AddMsg.TYPE_EMOJI: {
                        if (XTools.strEmpty(msg.Content) || msg.HasProductId > 0) {
                            //表情商店的表情，无法下载图片
                            WXEmoji wxEmoji = parseCommon(msg, new WXEmoji());
                            wxEmoji.setImgWidth(msg.ImgWidth);
                            wxEmoji.setImgHeight(msg.ImgHeight);
                            return wxEmoji;
                        } else {
                            //非表情商店的表情，下载图片
                            WXImage wxImage = parseCommon(msg, new WXImage());
                            wxImage.setImgWidth(msg.ImgWidth);
                            wxImage.setImgHeight(msg.ImgHeight);
                            wxImage.setImage(wxAPI.webwxgetmsgimg(msg.MsgId, "big"));
                            wxImage.setOrigin(wxImage.getImage());
                            return wxImage;
                        }
                    }
                    case RspSync.AddMsg.TYPE_OTHER: {
                        if (msg.AppMsgType == 2) {
                            WXImage wxImage = parseCommon(msg, new WXImage());
                            wxImage.setImgWidth(msg.ImgWidth);
                            wxImage.setImgHeight(msg.ImgHeight);
                            wxImage.setImage(wxAPI.webwxgetmsgimg(msg.MsgId, "big"));
                            wxImage.setOrigin(wxImage.getImage());
                            return wxImage;
                        } else if (msg.AppMsgType == 5) {
                            WXLink wxLink = parseCommon(msg, new WXLink());
                            wxLink.setLinkName(msg.FileName);
                            wxLink.setLinkUrl(msg.Url);
                            return wxLink;
                        } else if (msg.AppMsgType == 6) {
                            WXFile wxFile = parseCommon(msg, new WXFile());
                            wxFile.setFileId(msg.MediaId);
                            wxFile.setFileName(msg.FileName);
                            wxFile.setFileSize(XTools.strEmpty(msg.FileSize) ? 0 : Long.parseLong(msg.FileSize));
                            return wxFile;
                        } else if (msg.AppMsgType == 8) {
                            WXImage wxImage = parseCommon(msg, new WXImage());
                            wxImage.setImgWidth(msg.ImgWidth);
                            wxImage.setImgHeight(msg.ImgHeight);
                            wxImage.setImage(wxAPI.webwxgetmsgimg(msg.MsgId, "big"));
                            wxImage.setOrigin(wxImage.getImage());
                            return wxImage;
                        } else if (msg.AppMsgType == 2000) {
                            return parseCommon(msg, new WXMoney());
                        }
                        break;
                    }
                    case RspSync.AddMsg.TYPE_NOTIFY: {
                        WXNotify wxNotify = parseCommon(msg, new WXNotify());
                        wxNotify.setNotifyCode(msg.StatusNotifyCode);
                        wxNotify.setNotifyContact(msg.StatusNotifyUserName);
                        return wxNotify;
                    }
                    case RspSync.AddMsg.TYPE_SYSTEM: {
                        return parseCommon(msg, new WXSystem());
                    }
                    case RspSync.AddMsg.TYPE_REVOKE:
                        WXRevoke wxRevoke = parseCommon(msg, new WXRevoke());
                        Matcher idMatcher = REX_REVOKE_ID.matcher(wxRevoke.getContent());
                        if (idMatcher.find()) {
                            wxRevoke.setMsgId(Long.parseLong(idMatcher.group(1)));
                        }
                        Matcher replaceMatcher = REX_REVOKE_REPLACE.matcher(wxRevoke.getContent());
                        if (replaceMatcher.find()) {
                            wxRevoke.setMsgReplace(replaceMatcher.group(1));
                        }
                        return wxRevoke;
                    default:
                        break;
                }
            } catch (Exception e) {
                log.error("", e);
                XTools.logW(LOG_TAG, "消息解析失败");
            }
            return parseCommon(msg, new WXUnknown());
        }
    }
}
