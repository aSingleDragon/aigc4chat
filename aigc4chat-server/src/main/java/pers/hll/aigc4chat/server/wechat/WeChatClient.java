package pers.hll.aigc4chat.server.wechat;

import ch.qos.logback.core.testUtil.RandomUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import pers.hll.aigc4chat.base.constant.FilePath;
import pers.hll.aigc4chat.base.util.ImgTypeUtil;
import pers.hll.aigc4chat.base.util.XmlUtil;
import pers.hll.aigc4chat.entity.wechat.contact.Member;
import pers.hll.aigc4chat.entity.wechat.contact.WXContact;
import pers.hll.aigc4chat.entity.wechat.contact.WXGroup;
import pers.hll.aigc4chat.entity.wechat.contact.WXUser;
import pers.hll.aigc4chat.entity.wechat.message.*;
import pers.hll.aigc4chat.protocol.wechat.WeChatHttpClient;
import pers.hll.aigc4chat.protocol.wechat.constant.Cmd;
import pers.hll.aigc4chat.protocol.wechat.constant.MsgType;
import pers.hll.aigc4chat.protocol.wechat.constant.Op;
import pers.hll.aigc4chat.protocol.wechat.request.WebWxGetAvatarReq;
import pers.hll.aigc4chat.protocol.wechat.request.body.Contact;
import pers.hll.aigc4chat.protocol.wechat.request.body.Msg;
import pers.hll.aigc4chat.protocol.wechat.response.*;
import pers.hll.aigc4chat.protocol.wechat.response.webwxinit.User;
import pers.hll.aigc4chat.protocol.wechat.response.webwxsync.AddMsg;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 模拟网页微信客户端 测试用 有点丑陋 不要在意
 *
 * @author hll
 * @since 2024/03/10
 */
@Slf4j
public final class WeChatClient {

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

    private static final Pattern REX_GROUP_MSG = Pattern.compile("(@[0-9a-zA-Z]+):<br/>([\\s\\S]*)");

    private static final Pattern REX_REVOKE_ID = Pattern.compile("&lt;msgid&gt;(\\d+)&lt;/msgid&gt;");

    private static final Pattern REX_REVOKE_REPLACE =
            Pattern.compile("&lt;replacemsg&gt;&lt;!\\[CDATA\\[([\\s\\S]*)]]&gt;&lt;/replacemsg&gt;");

    private final WeChatThread weChatThread = new WeChatThread();

    private final WeChatContacts weChatContacts = new WeChatContacts();

    private final WeChatApi weChatApi = new WeChatApi();

    @Setter
    @Getter
    private WeChatListener weChatListener;

    private volatile int status = STATUS_READY;

    /**
     * 处理监听器，二维码事件
     *
     * @param qrcode 二维码地址
     */
    private void handleQRCode(@Nonnull String qrcode) {
        this.status = STATUS_SCAN;
        WeChatListener listener = this.weChatListener;
        if (listener != null) {
            listener.onQrCode(this, qrcode);
        }
    }

    /**
     * 处理监听器，头像事件
     *
     * @param base64Avatar base64编码头像
     */
    private void handleAvatar(@Nonnull String base64Avatar) {
        this.status = STATUS_PERMIT;
        WeChatListener listener = this.weChatListener;
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
        WeChatListener listener = this.weChatListener;
        if (listener != null) {
            listener.onFailure(this, reason);
        }
    }

    /**
     * 处理监听器，登录完成事件
     */
    private void handleLogin() {
        this.status = STATUS_WORKING;
        WeChatListener listener = this.weChatListener;
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
        WeChatListener listener = this.weChatListener;
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
        WeChatListener listener = this.weChatListener;
        if (listener != null) {
            listener.onContact(this, oldContact, newContact);
        }
    }

    /**
     * 处理监听器，退出登录事件
     */
    private void handleLogout() {
        this.status = STATUS_LOGOUT;
        WeChatListener listener = this.weChatListener;
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
        if (StringUtils.isNotEmpty(userNames)) {
            LinkedList<Contact> contacts = new LinkedList<>();
            for (String userName : userNames.split(",")) {
                if (StringUtils.isNotEmpty(userName)) {
                    contacts.add(new Contact(userName, ""));
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
    private void loadContacts(@Nonnull List<Contact> contacts, boolean useCache) {
        if (useCache) {
            // 不是群聊，并且已经获取过，就不再次获取
            contacts.removeIf(contact -> !contact.userName.startsWith("@@")
                    && weChatContacts.getContact(contact.userName) != null);
        }
        // 拆分成每次50个联系人分批获取
        if (contacts.size() > 50) {
            LinkedList<Contact> temp = new LinkedList<>();
            for (Contact contact : contacts) {
                temp.add(contact);
                if (temp.size() >= 50) {
                    WebWxBatchGetContactResp webWxBatchGetContactResp = weChatApi.webWxBatchGetContact(contacts);
                    for (User user : webWxBatchGetContactResp.getContactList()) {
                        weChatContacts.putContact(weChatApi.getHost(), user);
                    }
                    temp.clear();
                }
            }
            contacts = temp;
        }
        if (!contacts.isEmpty()) {
            WebWxBatchGetContactResp webWxBatchGetContactResp = weChatApi.webWxBatchGetContact(contacts);
            for (User user : webWxBatchGetContactResp.getContactList()) {
                weChatContacts.putContact(weChatApi.getHost(), user);
            }
        }
    }

    /**
     * 打印Cookie和登录信息
     */
    public void dump() {
        try {
            log.info("微信用户: {}", userMe().getNickName());
            StringBuilder sbLogin = new StringBuilder("登录信息：");
            sbLogin.append("\n\thost：").append(weChatApi.getHost());
            sbLogin.append("\n\tuin：").append(weChatApi.getUin());
            sbLogin.append("\n\tsid：").append(weChatApi.getSid());
            sbLogin.append("\n\tdataTicket：").append(weChatApi.getWebWxDataTicket());
            sbLogin.append("\n\tuuid：").append(weChatApi.getUuid());
            sbLogin.append("\n\tskey：").append(weChatApi.getSkey());
            sbLogin.append("\n\tpassTicket：").append(weChatApi.getPassTicket());
            sbLogin.append("\n\tsyncKey：").append(weChatApi.getSyncKey());
            sbLogin.append("\n\tsyncCheckKey：").append(weChatApi.getSyncCheckKey());
            log.info(sbLogin.toString().replace("%", "%%"));
        } catch (Exception e) {
            log.error("", e);
        }
    }

    /**
     * 启动客户端，注意：一个客户端类的实例只能被启动一次
     */
    public void startup() {
        weChatThread.start();
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
        weChatApi.webWxLogout();
        weChatThread.interrupt();
    }

    /**
     * 获取当前登录的用户信息
     *
     * @return 当前登录的用户信息
     */
    public WXUser userMe() {
        return weChatContacts.getMe();
    }

    /**
     * 根据userId获取用户好友
     *
     * @param userId 好友的id
     * @return 好友的信息
     */
    @Nullable
    public WXUser userFriend(@Nonnull String userId) {
        return weChatContacts.getFriend(userId);
    }

    /**
     * 获取用户所有好友
     *
     * @return 用户所有好友
     */
    @Nonnull
    public Map<String, WXUser> userFriends() {
        return weChatContacts.getFriends();
    }

    /**
     * 根据群id获取群信息
     *
     * @param groupId 群id
     * @return 群信息
     */
    @Nullable
    public WXGroup userGroup(@Nonnull String groupId) {
        return weChatContacts.getGroup(groupId);
    }

    /**
     * 获取用户所有群
     *
     * @return 用户所有群
     */
    @Nonnull
    public Map<String, WXGroup> userGroups() {
        return weChatContacts.getGroups();
    }

    /**
     * 根据联系人id获取用户联系人信息
     *
     * @param contactId 联系人id
     * @return 联系人信息
     */
    @Nullable
    public WXContact userContact(@Nonnull String contactId) {
        return weChatContacts.getContact(contactId);
    }

    /**
     * 发送文字消息
     *
     * @param wxContact 目标联系人
     * @param text      要发送的文字
     * @return 文本消息
     */
    public WXText sendText(@Nonnull WXContact wxContact, @Nonnull String text) {
        log.info("向({}: {})发送文字消息：{}}", wxContact.getUserName(), wxContact.getNickName(), text);
        WebWxSendMsgResp rspSendMsg = weChatApi.webWxSendMsg(new Msg(MsgType.TEXT, null, 0,
                text, null, weChatContacts.getMe().getUserName(), wxContact.getUserName()));
        WXText wxText = new WXText();
        // 自己给自己发送消息时, localId和msgId都为空字符串, parseLong会异常, 因此随机一个数
        wxText.setId(StringUtils.isBlank(rspSendMsg.getMsgId()) ? RandomUtil.getPositiveInt() : Long.parseLong(rspSendMsg.getMsgId()));
        wxText.setIdLocal(StringUtils.isBlank(rspSendMsg.getLocalId()) ? RandomUtil.getPositiveInt() : Long.parseLong(rspSendMsg.getLocalId()));
        wxText.setTimestamp(System.currentTimeMillis());
        wxText.setFromGroup(null);
        wxText.setFromUser(weChatContacts.getMe());
        wxText.setToContact(wxContact);
        wxText.setContent(text);
        return wxText;
    }
    /**
     * 发送位置消息
     * <p>
     * 经纬度坐标可以通过腾讯坐标拾取工具获得(<a href="https://lbs.qq.com/tool/getpoint">腾讯坐标工具</a>)
     * 其拾取的坐标默认格式为 lat,lon
     * </p>
     *
     * @param wxContact  目标联系人
     * @param oriContent 位置信息
     * @return 定位消息
     */
    @Nonnull
    public WXLocation sendLocation(@Nonnull WXContact wxContact, OriContent oriContent) {
        log.error("向({}: {})发送位置信息: {}", wxContact.getUserName(), wxContact.getNickName(), oriContent);
        WebWxSendMsgResp rspSendMsg = weChatApi.webWxSendMsg(new Msg(
                MsgType.LOCATION,
                null,
                0,
                XmlUtil.objectToXmlStr(oriContent),
                null,
                weChatContacts.getMe().getUserName(),
                wxContact.getUserName()));
        WXLocation wxLocation = new WXLocation();
        wxLocation.setId(Long.parseLong(rspSendMsg.getMsgId()));
        wxLocation.setIdLocal(Long.parseLong(rspSendMsg.getLocalId()));
        wxLocation.setTimestamp(System.currentTimeMillis());
        wxLocation.setFromGroup(null);
        wxLocation.setFromUser(weChatContacts.getMe());
        wxLocation.setToContact(wxContact);
        wxLocation.setContent(oriContent.toString());
        return wxLocation;
    }


    @Deprecated
    public WXText sendEmoji(@Nonnull WXContact wxContact, String content) {
        log.info("向({}: {})发送表情消息：{}}", wxContact.getUserName(), wxContact.getNickName());
        WebWxSendMsgResp rspSendMsg = weChatApi.webWxSendMsg(new Msg(
                MsgType.EMOJI,
                null,
                1,
                content,
                null,
                weChatContacts.getMe().getUserName(),
                wxContact.getUserName()));
        WXText wxText = new WXText();
        wxText.setId(Long.parseLong(rspSendMsg.getMsgId()));
        wxText.setIdLocal(Long.parseLong(rspSendMsg.getLocalId()));
        wxText.setTimestamp(System.currentTimeMillis());
        wxText.setFromGroup(null);
        wxText.setFromUser(weChatContacts.getMe());
        wxText.setToContact(wxContact);
        wxText.setContent(null);
        return wxText;
    }
    public WXVoice sendVoice(WXContact wxContact, String voiceFilePath) {
        log.info("向({}: {})发送语音消息：{}}", wxContact.getUserName(), wxContact.getNickName(), voiceFilePath);
        WebWxUploadMediaResp webWxUploadMediaResp = null;
        try {
            webWxUploadMediaResp = weChatApi.webWxUploadMedia(weChatContacts.getMe().getUserName(),
                    wxContact.getUserName(), new File(voiceFilePath), null, null);
        } catch (IOException e) {
            log.error("发送语音消息异常: ", e);
        }
        String mediaId = webWxUploadMediaResp.getMediaId();
        VoiceMsg voiceMsg = new VoiceMsg();
        voiceMsg.setVoice(new VoiceMsg.Voice(mediaId));
        voiceMsg.setMsgType("voice");
        voiceMsg.setCreateTime(System.currentTimeMillis());
        voiceMsg.setFromUserName(weChatContacts.getMe().getUserName());
        voiceMsg.setToUserName(wxContact.getUserName());
        String voiceContent = XmlUtil.objectToXmlStr(voiceMsg);
        WebWxSendMsgResp rspSendMsg = weChatApi.webWxSendMsg(new Msg(MsgType.VOICE, mediaId, 0,
                voiceContent, null, weChatContacts.getMe().getUserName(), wxContact.getUserName()));
        //WXVoice wxVoice = new WXVoice();
        //wxVoice.setId(Long.parseLong(rspSendMsg.getMsgId()));
        //wxVoice.setIdLocal(Long.parseLong(rspSendMsg.getLocalId()));
        //wxVoice.setTimestamp(System.currentTimeMillis());
        //wxVoice.setFromGroup(null);
        //wxVoice.setFromUser(weChatContacts.getMe());
        //wxVoice.setToContact(wxContact);
        //wxVoice.setContent(null);
        //return wxVoice;
        return null;
    }

    /**
     * 发送文件消息，可以是图片，动图，视频，文本等文件
     *
     * @param wxContact 目标联系人
     * @param filePath      要发送的文件
     * @return 图像或附件消息
     */
    @Nullable
    public WXMessage sendFile(@Nonnull WXContact wxContact, @Nonnull String filePath) {
        File file = new File(filePath);
        String suffix = ImgTypeUtil.fileSuffix(file);
        if ("mp4".equals(suffix) && file.length() >= 20L * 1024L * 1024L) {
            log.warn("向({}: {})发送的视频文件大于20M，无法发送", wxContact.getUserName(), wxContact.getNickName());
            return null;
        } else {
            try {
                log.info("向({}: {})发送文件: {}", wxContact.getUserName(), wxContact.getNickName(), file.getAbsolutePath());
                String mediaId = null, aesKey = null, signature = null;
                // 如果文件大于25M，则检查文件是否已经在微信服务器上
                if (file.length() >= 25L * 1024L * 1024L) {
                    WebWxCheckUploadResp rspCheckUpload = weChatApi.webWxCheckUpload(filePath, weChatContacts.getMe().getUserName(), wxContact.getUserName());
                    mediaId = rspCheckUpload.getMediaId();
                    aesKey = rspCheckUpload.getAseKey();
                    signature = rspCheckUpload.getSignature();
                }
                // 如果文件不在微信服务器上，则上传文件
                if (StringUtils.isEmpty(mediaId)) {
                    WebWxUploadMediaResp webWxUploadMediaResp = weChatApi.webWxUploadMedia(weChatContacts.getMe().getUserName(), wxContact.getUserName(), file, aesKey, signature);
                    mediaId = webWxUploadMediaResp.getMediaId();
                }
                if (StringUtils.isNotEmpty(mediaId)) {
                    switch (ImgTypeUtil.fileType(file)) {
                        case "pic": {
                            WebWxSendMsgResp webWxSendMsgResp = weChatApi.webWxSendMsgImg(new Msg(MsgType.IMAGE, mediaId,
                                    null, "", signature, weChatContacts.getMe().getUserName(), wxContact.getUserName()));
                            WXImage wxImage = new WXImage();
                            wxImage.setId(Long.parseLong(webWxSendMsgResp.getMsgId()));
                            wxImage.setIdLocal(Long.parseLong(webWxSendMsgResp.getLocalId()));
                            wxImage.setTimestamp(System.currentTimeMillis());
                            wxImage.setFromGroup(null);
                            wxImage.setFromUser(weChatContacts.getMe());
                            wxImage.setToContact(wxContact);
                            wxImage.setImgWidth(0);
                            wxImage.setImgHeight(0);
                            wxImage.setImage(filePath);
                            wxImage.setOrigin(filePath);
                            return wxImage;
                        }
                        case "video": {
                            WebWxSendMsgResp rspSendMsg = weChatApi.webWxSendVideoMsg(new Msg(MsgType.VIDEO, mediaId, null, "", signature, weChatContacts.getMe().getUserName(), wxContact.getUserName()));
                            WXVideo wxVideo = new WXVideo();
                            wxVideo.setId(Long.parseLong(rspSendMsg.getMsgId()));
                            wxVideo.setIdLocal(Long.parseLong(rspSendMsg.getLocalId()));
                            wxVideo.setTimestamp(System.currentTimeMillis());
                            wxVideo.setFromGroup(null);
                            wxVideo.setFromUser(weChatContacts.getMe());
                            wxVideo.setToContact(wxContact);
                            wxVideo.setImgWidth(0);
                            wxVideo.setImgHeight(0);
                            wxVideo.setImage(weChatApi.webWxGetMsgImg(wxVideo.getId(), "slave"));
                            wxVideo.setVideoLength(0);
                            wxVideo.setVideo(filePath);
                            return wxVideo;
                        }
                        default:
                            if ("gif".equals(suffix)) {
                                WebWxSendMsgResp rspSendMsg = weChatApi.webWxSendEmoticon(new Msg(MsgType.EMOJI, mediaId, 2, "", signature, weChatContacts.getMe().getUserName(), wxContact.getUserName()));
                                WXImage wxImage = new WXImage();
                                wxImage.setId(Long.parseLong(rspSendMsg.getMsgId()));
                                wxImage.setIdLocal(Long.parseLong(rspSendMsg.getLocalId()));
                                wxImage.setTimestamp(System.currentTimeMillis());
                                wxImage.setFromGroup(null);
                                wxImage.setFromUser(weChatContacts.getMe());
                                wxImage.setToContact(wxContact);
                                wxImage.setImgWidth(0);
                                wxImage.setImgHeight(0);
                                wxImage.setImage(filePath);
                                wxImage.setOrigin(filePath);
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
                                sbAppMsg.append("<totallen>").append(filePath.length()).append("</totallen>");
                                sbAppMsg.append("<attachid>").append(mediaId).append("</attachid>");
                                sbAppMsg.append("<fileext>").append(StringUtils.isEmpty(suffix) ? "undefined" : suffix).append("</fileext>");
                                sbAppMsg.append("</appattach>");
                                sbAppMsg.append("<extinfo></extinfo>");
                                sbAppMsg.append("</appmsg>");
                                WebWxSendMsgResp rspSendMsg = weChatApi.webWxSendAppMsg(new Msg(6, null, null, sbAppMsg.toString(), signature, weChatContacts.getMe().getUserName(), wxContact.getUserName()));
                                WXFile wxFile = new WXFile();
                                wxFile.setId(Long.parseLong(rspSendMsg.getMsgId()));
                                wxFile.setIdLocal(Long.parseLong(rspSendMsg.getLocalId()));
                                wxFile.setTimestamp(System.currentTimeMillis());
                                wxFile.setFromGroup(null);
                                wxFile.setFromUser(weChatContacts.getMe());
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
                    log.error("向({}: {})发送的文件发送失败", wxContact.getUserName(), wxContact.getNickName());
                }
            } catch (IOException e) {
                log.error("向({}: {})发送的文件发送失败, 异常信息: ", wxContact.getUserName(), wxContact.getNickName(), e);
            }
        }
        return null;
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
        WXContact contact = weChatContacts.getContact(contactId);
        if (contact instanceof WXGroup wxGroup) {
            List<Contact> contacts = new LinkedList<>();
            for (Member member : wxGroup.getMembers().values()) {
                contacts.add(new Contact(member.getId(), wxGroup.getUserName()));
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
        WeChatHttpClient.get(new WebWxGetAvatarReq(wxContact.getAvatarUrl())
                .setFileStreamAvailable(true)
                .setFileStreamSavePath(FilePath.WECHAT_ME)
                .build());
        wxContact.setAvatarFile(FilePath.WECHAT_ME);
        return wxContact;
    }

    /**
     * 获取图片消息的大图
     *
     * @param wxImage 要获取大图的图片消息
     * @return 获取大图后的图片消息
     */
    @Nonnull
    public WXImage fetchImage(@Nonnull WXImage wxImage) throws IOException {
        wxImage.setOrigin(weChatApi.webWxGetMsgImg(wxImage.getId(), "big"));
        return wxImage;
    }

    /**
     * 获取语音消息的语音文件
     *
     * @param wxVoice 语音消息
     * @return 获取语音文件后的语音消息
     */
    @Nonnull
    public WXVoice fetchVoice(@Nonnull WXVoice wxVoice) throws IOException {
        wxVoice.setVoice(weChatApi.webWxGetVoice(wxVoice.getId()));
        return wxVoice;
    }

    /**
     * 获取视频消息的视频文件
     *
     * @param wxVideo 视频消息
     * @return 获取视频文件后的视频消息
     */
    @Nonnull
    public WXVideo fetchVideo(@Nonnull WXVideo wxVideo) throws IOException {
        wxVideo.setVideo(weChatApi.webWxGetVideo(wxVideo.getId()));
        return wxVideo;
    }

    /**
     * 获取文件消息的附件文件
     *
     * @param wxFile 文件消息
     * @return 获取附件文件后的文件消息
     */
    @Nonnull
    public WXFile fetchFile(@Nonnull WXFile wxFile) throws IOException {
        wxFile.setFile(weChatApi.webWxGetMedia(
                wxFile.getId(), wxFile.getFileName(), wxFile.getFileId(), wxFile.getFromUser().getUserName()));
        return wxFile;
    }

    /**
     * 撤回消息
     *
     * @param wxMessage 需要撤回的微信消息
     */
    public void revokeMsg(@Nonnull WXMessage wxMessage) {
        log.info("撤回向({}:{})发送的消息: {}, {}}", wxMessage.getToContact().getUserName(), wxMessage.getToContact().getNickName(),
                wxMessage.getIdLocal(), wxMessage.getId());
        weChatApi.webWxRevokeMsg(wxMessage.getIdLocal(), wxMessage.getId(), wxMessage.getToContact().getUserName());
    }

    /**
     * 同意好友申请
     *
     * @param wxVerify 好友验证消息
     */
    public void passVerify(@Nonnull WXVerify wxVerify) {
        log.info("通过({}:{})的好友申请", wxVerify.getUserId(), wxVerify.getUserName());
        weChatApi.webWxVerifyUser(3, wxVerify.getUserId(), wxVerify.getTicket(), "");
    }

    /**
     * 修改用户备注名
     *
     * @param wxUser 目标用户
     * @param remark 备注名称
     */
    public void editRemark(@Nonnull WXUser wxUser, @Nonnull String remark) {
        log.info("修改({}:{})的备注: {}", wxUser.getUserName(), wxUser.getNickName(), remark);
        weChatApi.webWxOpLog(Cmd.CMD_REMARK, Op.OP_NONE, wxUser.getUserName(), remark);
    }

    /**
     * 设置联系人置顶状态
     *
     * @param wxContact 需要设置置顶状态的联系人
     * @param isTop     是否置顶
     */
    public void topContact(@Nonnull WXContact wxContact, boolean isTop) {
        log.info("设置({}, {})的置顶状态: {}", wxContact.getUserName(), wxContact.getNickName(), isTop);
        weChatApi.webWxOpLog(Cmd.CMD_TOP, isTop ? Op.OP_TOP_TRUE : Op.OP_TOP_FALSE, wxContact.getUserName(), null);
    }

    /**
     * 修改聊天室名称
     *
     * @param wxGroup 目标聊天室
     * @param name    目标名称
     */
    public void setGroupName(@Nonnull WXGroup wxGroup, @Nonnull String name) {
        log.info("将群({}:{})的名称修改为: {}", wxGroup.getUserName(), wxGroup.getNickName(), name);
        weChatApi.webWxUpdateChatRoom(wxGroup.getUserName(), "modtopic", name, new LinkedList<>());
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
                log.info("正在登录");
                String loginErr = login();
                if (StringUtils.isNotEmpty(loginErr)) {
                    log.error("登录出现错误: {}", loginErr);
                    handleFailure(loginErr);
                }
                // 用户初始化
                log.info("开始初始化...");
                String initErr = initial();
                if (StringUtils.isNotEmpty(initErr)) {
                    log.error("初始化出现错误：{}", initErr);
                    handleFailure(initErr);
                    break;
                }
                handleLogin();
                // 同步消息
                log.info("开始监听消息...");
                String listenErr = listen();
                if (StringUtils.isNotEmpty(listenErr)) {
                    if (loginCount++ > 10) {
                        handleFailure(listenErr);
                        break;
                    } else {
                        continue;
                    }
                }
                // 退出登录
                log.info("正在退出登录");
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
                if (StringUtils.isEmpty(weChatApi.getSid())) {
                    String qrCode = weChatApi.jsLogin();
                    handleQRCode(qrCode);
                    while (true) {
                        LoginResp loginResp = weChatApi.login();
                        switch (loginResp.getCode()) {
                            case 200:
                                log.info("已授权登录");
                                weChatApi.webWxNewLoginPage(loginResp.getRedirectUri());
                                return null;
                            case 201:
                                log.info("已扫描二维码");
                                handleAvatar(loginResp.getUserAvatar());
                                break;
                            case 408:
                                log.info("等待授权登录");
                                break;
                            default:
                                log.warn("登录超时...");
                                return LOGIN_TIMEOUT;
                        }
                    }
                } else {
                    return null;
                }
            } catch (Exception e) {
                log.error("登录异常:", e);
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
                if (WeChatHttpClient.getCookieStore() != null) {
                    WeChatHttpClient.getCookieStore().getCookies().forEach(httpCookie -> {
                        if ("webwx_data_ticket".equalsIgnoreCase(httpCookie.getName())) {
                            weChatApi.setWebWxDataTicket(httpCookie.getValue());
                            log.info("已成功设置webwx_data_ticket:{}", httpCookie.getValue());
                        }
                    });
                }

                // 获取自身信息
                log.info("正在获取自身信息...");
                WebWxInitResp webWxInitResp = weChatApi.webWxInit();
                weChatContacts.setMe(weChatApi.getHost(), webWxInitResp.getUser());

                // 获取并保存最近联系人
                log.info("正在获取并保存最近联系人...");
                loadContacts(webWxInitResp.getChatSet(), true);

                // 发送初始化状态信息
                WebWxStatusNotifyResp webWxStatusNotifyResp =
                        weChatApi.webWxStatusNotify(weChatContacts.getMe().getUserName(), WXNotify.NOTIFY_INITED);
                log.info("状态通知结果:{}", webWxStatusNotifyResp);

                // 获取好友、保存的群聊、公众号列表。
                // 这里获取的群没有群成员，不过也不能用fetchContact方法暴力获取群成员，因为这样数据量会很大
                log.info("正在获取好友、群、公众号列表...");
                WebWxGetContactResp webWxGetContactResp = weChatApi.webWxGetContact();
                for (User user : webWxGetContactResp.getMemberList()) {
                    weChatContacts.putContact(weChatApi.getHost(), user);
                }
                return null;
            } catch (Exception e) {
                log.error("初始化异常:", e);
                return INIT_EXCEPTION;
            }
        }

        /**
         * 循环同步消息
         *
         * @return 同步消息的异常原因，为null表示正常结束
         */
        private String listen() {
            int retryCount = 0;
            try {
                while (!isInterrupted()) {
                    SyncCheckResp syncCheckResp;
                    try {
                        log.info("正在监听消息...");
                        syncCheckResp = weChatApi.syncCheck();
                    } catch (Exception e) {
                        log.error("同步检查失败: ", e);
                        if (retryCount++ < 5) {
                            log.warn("监听异常，重试第{}次", retryCount);
                            continue;
                        } else {
                            log.error("监听异常，重试次数过多");
                            return LISTEN_EXCEPTION;
                        }
                    }
                    retryCount = 0;
                    if (Objects.isNull(syncCheckResp)) {
                        log.error("请求超时，当前监听异常");
                        return null;
                    } else if (syncCheckResp.getRetCode() > 0) {
                        log.warn("停止监听信息, 同步检查响应信息: {}", syncCheckResp);
                        return null;
                    } else if (syncCheckResp.getSelector() > 0) {
                        WebWxSyncResp webWxSyncResp = weChatApi.webWxSync();
                        if (webWxSyncResp.getDelContactList() != null) {
                            // 删除好友立刻触发
                            // 删除群后的任意一条消息触发
                            // 被移出群不会触发（会收到一条被移出群的addMsg）
                            for (User user : webWxSyncResp.getDelContactList()) {
                                WXContact oldContact = weChatContacts.rmvContact(user.getUserName());
                                if (oldContact != null && StringUtils.isNotEmpty(oldContact.getNickName())) {
                                    log.info("删除联系人:{}", user.getUserName());
                                    handleContact(oldContact, null);
                                }
                            }
                        }
                        if (webWxSyncResp.getModContactList() != null) {
                            // 添加好友立刻触发
                            // 被拉入已存在的群立刻触发
                            // 被拉入新群第一条消息触发（同时收到2条addMsg，一条被拉入群，一条聊天消息）
                            //  群里有人加入或群里踢人或修改群信息之后第一条信息触发
                            for (User user : webWxSyncResp.getModContactList()) {
                                //由于在这里获取到的联系人（无论是群还是用户）的信息是不全的，所以使用接口重新获取
                                WXContact oldContact = weChatContacts.getContact(user.getUserName());
                                if (oldContact != null && StringUtils.isEmpty(oldContact.getNickName())) {
                                    weChatContacts.rmvContact(user.getUserName());
                                    oldContact = null;
                                }
                                WXContact newContact = fetchContact(user.getUserName());
                                if (newContact != null && StringUtils.isEmpty(newContact.getNickName())) {
                                    weChatContacts.rmvContact(user.getUserName());
                                    newContact = null;
                                }
                                if (oldContact != null || newContact != null) {
                                    log.info("变更联系人: {}", user.getUserName());
                                    handleContact(oldContact, newContact);
                                }
                            }
                        }
                        if (webWxSyncResp.getAddMsgList() != null) {
                            for (AddMsg addMsg : webWxSyncResp.getAddMsgList()) {
                                // 接收到的消息，文字、图片、语音、地理位置等等
                                WXMessage wxMessage = parseMessage(addMsg);
                                log.info("收到消息:{}", wxMessage);
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
                TimeUnit.SECONDS.sleep(1);
                return null;
            } catch (Exception e) {
                log.error("监听消息异常: ", e);
                Thread.currentThread().interrupt();
                return LISTEN_EXCEPTION;
            }
        }

        @Nonnull
        private <T extends WXMessage> T parseCommon(@Nonnull AddMsg msg, @Nonnull T message) {
            message.setId(msg.getMsgId());
            message.setIdLocal(msg.getMsgId());
            message.setTimestamp(msg.getCreateTime() * 1000);
            if (msg.getFromUserName().startsWith("@@")) {
                // 是群消息
                message.setFromGroup((WXGroup) weChatContacts.getContact(msg.getFromUserName()));
                if (message.getFromGroup() == null
                        || !message.getFromGroup().isDetail()
                        || message.getFromGroup().getMembers().isEmpty()) {
                    // 如果群不存在，或者是未获取成员的群。获取并保存群的详细信息
                    message.setFromGroup((WXGroup) fetchContact(msg.getFromUserName()));
                }
                Matcher mGroupMsg = REX_GROUP_MSG.matcher(msg.getContent());
                if (mGroupMsg.matches()) {
                    // 是群成员发送的消息
                    message.setFromUser((WXUser) weChatContacts.getContact(mGroupMsg.group(1)));
                    if (message.getFromUser() == null) {
                        // 未获取成员。首先获取并保存群的详细信息，然后获取群成员信息
                        fetchContact(msg.getFromUserName());
                        message.setFromUser((WXUser) weChatContacts.getContact(mGroupMsg.group(1)));
                    }
                    message.setToContact(weChatContacts.getContact(msg.getToUserName()));
                    if (message.getToContact() == null) {
                        message.setToContact(fetchContact(msg.getToUserName()));
                    }
                    message.setContent(mGroupMsg.group(2));
                } else {
                    // 不是群成员发送的消息
                    message.setFromUser(null);
                    message.setToContact(weChatContacts.getContact(msg.getToUserName()));
                    if (message.getToContact() == null) {
                        message.setToContact(fetchContact(msg.getToUserName()));
                    }
                    message.setContent(msg.getContent());
                }
            } else {
                // 不是群消息
                message.setFromGroup(null);
                message.setFromUser((WXUser) weChatContacts.getContact(msg.getFromUserName()));
                if (message.getFromUser() == null) {
                    // 联系人不存在（一般不会出现这种情况），手动获取联系人
                    message.setFromUser((WXUser) fetchContact(msg.getFromUserName()));
                }
                message.setToContact(weChatContacts.getContact(msg.getToUserName()));
                if (message.getToContact() == null) {
                    message.setToContact(fetchContact(msg.getToUserName()));
                }
                message.setContent(msg.getContent());
            }
            return message;
        }

        @Nonnull
        private WXMessage parseMessage(@Nonnull AddMsg msg) {
            try {
                switch (msg.getMsgType()) {
                    case MsgType.TEXT: {
                        if (msg.getSubMsgType() == 0) {
                            return parseCommon(msg, new WXText());
                        } else if (msg.getSubMsgType() == MsgType.LOCATION) {
                            WXLocation wxLocation = parseCommon(msg, new WXLocation());
                            wxLocation.setLocationName(wxLocation.getContent().substring(0, wxLocation.getContent().indexOf(':')));
                            wxLocation.setLocationImage(String.format("https://%s%s", weChatApi.getHost(),
                                    wxLocation.getContent().substring(wxLocation.getContent().indexOf(':') + ":<br/>".length())));
                            wxLocation.setLocationUrl(msg.getUrl());
                            wxLocation.setOriContent(XmlUtil.xmlStrToObject(msg.getOriContent(), OriContent.class));
                            return wxLocation;
                        }
                        break;
                    }
                    case MsgType.IMAGE: {
                        WXImage wxImage = parseCommon(msg, new WXImage());
                        wxImage.setImgWidth(msg.getImgWidth());
                        wxImage.setImgHeight(msg.getImgHeight());
                        wxImage.setImage(weChatApi.webWxGetMsgImg(msg.getMsgId(), "slave"));
                        return wxImage;
                    }
                    case MsgType.VOICE: {
                        WXVoice wxVoice = parseCommon(msg, new WXVoice());
                        wxVoice.setVoiceLength(msg.getVoiceLength());
                        wxVoice.setVoice(weChatApi.webWxGetVoice(msg.getMsgId()));
                        wxVoice.setMediaId(msg.getMediaId());
                        return wxVoice;
                    }
                    case MsgType.VERIFY: {
                        WXVerify wxVerify = parseCommon(msg, new WXVerify());
                        wxVerify.setUserId(msg.getRecommendInfo().getUserName());
                        wxVerify.setUserName(msg.getRecommendInfo().getNickName());
                        wxVerify.setSignature(msg.getRecommendInfo().getSignature());
                        wxVerify.setProvince(msg.getRecommendInfo().getProvince());
                        wxVerify.setCity(msg.getRecommendInfo().getCity());
                        wxVerify.setGender(msg.getRecommendInfo().getSex());
                        wxVerify.setVerifyFlag(msg.getRecommendInfo().getVerifyFlag());
                        wxVerify.setTicket(msg.getRecommendInfo().getTicket());
                        return wxVerify;
                    }
                    case MsgType.RECOMMEND: {
                        WXRecommend wxRecommend = parseCommon(msg, new WXRecommend());
                        wxRecommend.setUserId(msg.getRecommendInfo().getUserName());
                        wxRecommend.setUserName(msg.getRecommendInfo().getNickName());
                        wxRecommend.setGender(msg.getRecommendInfo().getSex());
                        wxRecommend.setSignature(msg.getRecommendInfo().getSignature());
                        wxRecommend.setProvince(msg.getRecommendInfo().getProvince());
                        wxRecommend.setCity(msg.getRecommendInfo().getCity());
                        wxRecommend.setVerifyFlag(msg.getRecommendInfo().getVerifyFlag());
                        return wxRecommend;
                    }
                    case MsgType.VIDEO: {
                        // 视频貌似可以分片，后期测试
                        WXVideo wxVideo = parseCommon(msg, new WXVideo());
                        wxVideo.setImgWidth(msg.getImgWidth());
                        wxVideo.setImgHeight(msg.getImgHeight());
                        wxVideo.setVideoLength(msg.getPlayLength());
                        wxVideo.setImage(weChatApi.webWxGetMsgImg(msg.getMsgId(), "slave"));
                        return wxVideo;
                    }
                    case MsgType.EMOJI: {
                        if (StringUtils.isEmpty(msg.getContent()) || msg.getHasProductId() != 0) {
                            // 表情商店的表情，无法下载图片 应该要利用这msgId去获取这个表情的数据
                            WXEmoji wxEmoji = parseCommon(msg, new WXEmoji());
                            wxEmoji.setImgWidth(msg.getImgWidth());
                            wxEmoji.setImgHeight(msg.getImgHeight());
                            return wxEmoji;
                        } else {
                            // 非表情商店的表情，下载图片
                            WXImage wxImage = parseCommon(msg, new WXImage());
                            wxImage.setImgWidth(msg.getImgWidth());
                            wxImage.setImgHeight(msg.getImgHeight());
                            wxImage.setContent(wxImage.getContent());
                            wxImage.setImage(weChatApi.webWxGetMsgImg(msg.getMsgId(), "slave"));
                            wxImage.setOrigin(wxImage.getImage());
                            return wxImage;
                        }
                    }
                    case MsgType.OTHER: {
                        if (msg.getAppMsgType() == 2) {
                            WXImage wxImage = parseCommon(msg, new WXImage());
                            wxImage.setImgWidth(msg.getImgWidth());
                            wxImage.setImgHeight(msg.getImgHeight());
                            wxImage.setImage(weChatApi.webWxGetMsgImg(msg.getMsgId(), "big"));
                            wxImage.setOrigin(wxImage.getImage());
                            return wxImage;
                        } else if (msg.getAppMsgType() == 5) {
                            WXLink wxLink = parseCommon(msg, new WXLink());
                            wxLink.setLinkName(msg.getFileName());
                            wxLink.setLinkUrl(msg.getUrl());
                            return wxLink;
                        } else if (msg.getAppMsgType() == 6) {
                            WXFile wxFile = parseCommon(msg, new WXFile());
                            wxFile.setFileId(msg.getMediaId());
                            wxFile.setFileName(msg.getFileName());
                            wxFile.setFileSize(StringUtils.isEmpty(msg.getFileSize()) ? 0 : Long.parseLong(msg.getFileSize()));
                            return wxFile;
                        } else if (msg.getAppMsgType() == 8) {
                            WXImage wxImage = parseCommon(msg, new WXImage());
                            wxImage.setImgWidth(msg.getImgWidth());
                            wxImage.setImgHeight(msg.getImgHeight());
                            wxImage.setImage(weChatApi.webWxGetMsgImg(msg.getMsgId(), "big"));
                            wxImage.setOrigin(wxImage.getImage());
                            return wxImage;
                        } else if (msg.getAppMsgType() == 2000) {
                            return parseCommon(msg, new WXMoney());
                        }
                        break;
                    }
                    case MsgType.READ: {
                        WXNotify wxNotify = parseCommon(msg, new WXNotify());
                        wxNotify.setNotifyCode(msg.getStatusNotifyCode());
                        wxNotify.setNotifyContact(msg.getStatusNotifyUserName());
                        return wxNotify;
                    }
                    case MsgType.SYSTEM: {
                        return parseCommon(msg, new WXSystem());
                    }
                    case MsgType.REVOKE:
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
                log.error("消息解析失败", e);
            }
            return parseCommon(msg, new WXUnknown());
        }
    }
}
