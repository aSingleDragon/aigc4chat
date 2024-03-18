package pers.hll.aigc4chat.server.wechat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import pers.hll.aigc4chat.common.base.config.XConfigTools;
import pers.hll.aigc4chat.common.base.constant.FilePath;
import pers.hll.aigc4chat.common.base.http.executor.XHttpExecutor;
import pers.hll.aigc4chat.common.base.util.BaseUtil;
import pers.hll.aigc4chat.common.base.util.ImageTypeUtils;
import pers.hll.aigc4chat.common.base.util.QRCodeUtil;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.WeChatHttpClient;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.constant.WXQueryValue;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.request.*;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.request.body.BaseRequestBody;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.request.body.Contact;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.request.body.Msg;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.response.*;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.response.webwxinit.SyncKey;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static pers.hll.aigc4chat.common.protocol.wechat.protocol.constant.WXEndPoint.*;

/**
 * 网页版微信全部接口
 */
@Data
@Slf4j
final class WeChatApi {

    public static final String CFG_WORKDIR = WeChatClient.CFG_PREFIX + "workdir";

    public static final String CFG_WORKDIR_DEFAULT = "";

    private static final Gson GSON = new GsonBuilder().disableHtmlEscaping().create();

    private static final String[] HOSTS = {"wx.qq.com", "wx2.qq.com", "wx8.qq.com", "web.wechat.com", "web2.wechat.com"};

    private final AtomicBoolean firstLogin = new AtomicBoolean(true);

    private final XHttpExecutor httpExecutor = XConfigTools.supply(WXHttpExecutor.class.getName());

    private String host;

    private String uin;

    private String sid;

    private String dataTicket;

    private File folder = new File("/Users/hll/IdeaProjects/aigc/aigc4chat/file");

    private long time = 0;

    private int file = 0;

    private String uuid;

    private String skey;

    private String passTicket;

    private SyncKey syncKey;

    private SyncKey syncCheckKey;

    private String deviceId;

    /**
     * 获取登录二维码
     *
     * @return 登录二维码网址
     */
    public String jsLogin() {
        JsLoginResp jsLoginResp = WeChatHttpClient.get(new JsLoginReq(JS_LOGIN).build());
        if (200 != jsLoginResp.getCode()) {
            throw new IllegalStateException("获取登录二维码出错");
        } else {
            this.uuid = jsLoginResp.getUuid();
            String qrCodeUri = String.format(QR_CODE, uuid);
            QRCodeUtil.writeInImage(FilePath.LOGIN_QR_CODE, qrCodeUri);
            try {
                Desktop.getDesktop().open(new File(FilePath.LOGIN_QR_CODE));
            } catch (IOException e) {
                log.error("打开二维码图片失败:", e);
            }
            return qrCodeUri;
        }
    }

    /**
     * 监听登录，循环请求该接口，如果用户扫描或授权登录，该接口立即返回，否则将会在25秒后返回
     *
     * @return 监听结果，code=200用户授权登录，code=201用户扫描二维码，code=408等待用户扫描或授权，其他则表示登录超时
     */
    public LoginResp login() {
        LoginResp loginResp = WeChatHttpClient.get(new LoginReq(LOGIN)
                .setFirstLogin(firstLogin.getAndSet(false) ? 1 : 0)
                .setUuid(uuid)
                .build());
        this.time = BaseUtil.getEpochSecond() * 1000;
        if (StringUtils.isNotEmpty(loginResp.getRedirectUri())) {
            for (String h : HOSTS) {
                if (loginResp.getRedirectUri().contains(h)) {
                    this.host = h;
                    break;
                }
            }
            if (StringUtils.isEmpty(host)) {
                throw new IllegalStateException("未知主机");
            }
        }
        return loginResp;
    }

    /**
     * 用户登录，返回uin,sid等重要信息，如果该接口返回数据为空，则uin，sid等数据在cookie中获取
     * https://wx2.qq.com/cgi-bin/mmwebwx-bin/webwxnewloginpage?ticket=Awa59KcjrwCuCMeaY5NXxcUy@qrticket_0&uuid=gd5NPaW08Q==&lang=zh_CN&scan=1710077422
     *
     * @param url 登录url
     */
    public void webWxNewLoginPage(String url) {
        WxNewLoginPageResp wxNewLoginPageResp = WeChatHttpClient.get(new WxNewLoginPageReq(url)
                .setRedirectsEnabled(false)
                .build());
        this.uin = wxNewLoginPageResp.getWxUin();
        this.sid = wxNewLoginPageResp.getWxSid();
        this.skey = wxNewLoginPageResp.getSKey() == null ? "" : wxNewLoginPageResp.getSKey();
        this.passTicket = wxNewLoginPageResp.getPassTicket();
        this.deviceId = BaseUtil.createDeviceId();
    }

    /**
     * 初始化，获取自身信息，好友列表，活跃群等
     *
     * @return 初始化结果
     */
    public WebWxInitResp webWxInit() {
        WebWxInitResp webWxInitResp = WeChatHttpClient.post(new WebWxInitReq(String.format(WEB_WX_INIT, host))
                .setPassTicket(passTicket)
                .setBaseRequestBody(new BaseRequestBody(uin, sid, skey))
                .build());
        this.skey = webWxInitResp.getSKey();
        if (this.skey == null) {
            this.skey = "";
        }
        this.syncKey = webWxInitResp.getSyncKey();
        return webWxInitResp;
    }


    /**
     * 状态更新接口，登录登出，消息已读
     *
     * @param userName   目标联系人userName
     * @param notifyCode 状态码
     * @return 接口调用结果
     */
    public WebWxStatusNotifyResp webWxStatusNotify(String userName, int notifyCode) {
        return WeChatHttpClient.post(new WebWxStatusNotifyReq(String.format(WEB_WX_STATUS_NOTIFY, host))
                .setPassTicket(passTicket)
                .setBaseRequestBody(new BaseRequestBody(uin, sid, skey))
                .setNotifyCode(notifyCode)
                .setUserName(userName)
                .build());
    }

    /**
     * 获取联系人列表
     *
     * @return 联系人列表
     */
    public WebWxGetContactResp webWxGetContact() {
        return WeChatHttpClient.get(new WebWxGetContactReq(String.format(WEB_WX_GET_GET_CONTACT, host))
                .setSeq(0)
                .setSKey(skey)
                .setPassTicket(passTicket)
                .build());
    }

    /**
     * 批量获取联系人详细信息
     *
     * @param contactList 要获取的联系人列表
     * @return 联系人的详细信息
     */
    public WebWxBatchGetContactResp webWxBatchGetContact(List<Contact> contactList) {
        String url = String.format(WEB_WX_BATCH_GET_CONTACT, host);
        return WeChatHttpClient.post(new WebWxBatchGetContactReq(url)
                .setPassTicket(this.passTicket)
                .setCount(contactList.size())
                .setList(contactList)
                .setBaseRequestBody(new BaseRequestBody(uin, sid, skey))
                .build());
    }

    /**
     * 同步检查接口，需要无限循环请求该接口，如果有消息要同步，则该接口立即返回并携带参数，否则将在60秒后返回
     *
     * @return 检查结果
     */
    public SyncCheckResp syncCheck() {
        return WeChatHttpClient.get(new SyncCheckReq(String.format(SYNC_CHECK, host))
                .setSKey(skey)
                .setSId(sid)
                .setUin(uin)
                .setDeviceId(BaseUtil.createDeviceId())
                .setSyncKey(this.syncCheckKey != null ? this.syncCheckKey : this.syncKey)
                .setLoginTime(time++)
                .build());
    }

    /**
     * 同步接口，将服务端数据同步到本地，并更新本地SyncKey
     *
     * @return 获取到的数据
     */
    public WebWxSyncResp webWxSync() {
        WebWxSyncResp webWxSyncResp = WeChatHttpClient.post(new WebWxSyncReq(String.format(WEB_WX_SYNC, host))
                .setSId(sid)
                .setSKey(skey)
                .setPassTicket(passTicket)
                .setSyncKey(this.syncCheckKey != null ? this.syncCheckKey : this.syncKey)
                .setBaseRequestBody(new BaseRequestBody(uin, sid, skey))
                .build());
        if (webWxSyncResp.getSyncKey() != null
                && webWxSyncResp.getSyncKey().getList() != null
                && webWxSyncResp.getSyncKey().getCount() > 0) {
            this.syncKey = webWxSyncResp.getSyncKey();
        }
        if (webWxSyncResp.getSyncCheckKey() != null
                && webWxSyncResp.getSyncCheckKey().getList() != null
                && webWxSyncResp.getSyncCheckKey().getCount() > 0) {
            this.syncCheckKey = webWxSyncResp.getSyncCheckKey();
        }
        return webWxSyncResp;
    }

    /**
     * 发送消息接口
     *
     * @param msg 需要发送的消息
     * @return 发送的结果
     */
    public WebWxSendMsgResp webWxSendMsg(Msg msg) {
        String id = String.valueOf(BaseUtil.getEpochSecond() * 10000);
        msg.setClientMsgId(id);
        msg.setLocalId(id);
        return WeChatHttpClient.post(new WebWxSendMsgReq(String.format(WEB_WX_SEND_MSG, host))
                .setMsg(msg)
                .setBaseRequestBody(new BaseRequestBody(uin, sid, skey))
                .build());
    }

    /**
     * 发送图片消息
     *
     * @param msg 需要发送的图片消息
     * @return 发送的结果
     */
    public WebWxSendMsgResp webWxSendMsgImg(Msg msg) {
        String id = String.valueOf(BaseUtil.getEpochSecond() * 10000);
        msg.setClientMsgId(id);
        msg.setLocalId(id);
        return WeChatHttpClient.post(new WebWxSendMsgFileReq(String.format(WEB_WX_SEND_MSG_IMG, host))
                .setMsg(msg)
                .setFun(WXQueryValue.ASYNC)
                .setBaseRequestBody(new BaseRequestBody(uin, sid, skey))
                .build());
    }

    /**
     * 发送视频消息
     *
     * @param msg 需要发送的视频消息
     * @return 发送的结果
     */
    public WebWxSendMsgResp webWxSendVideoMsg(Msg msg) {
        String id = String.valueOf(BaseUtil.getEpochSecond() * 10000);
        msg.setClientMsgId(id);
        msg.setLocalId(id);
        return WeChatHttpClient.post(new WebWxSendMsgFileReq(String.format(WEB_WX_SEND_VIDEO_MSG, host))
                .setMsg(msg)
                .setFun(WXQueryValue.ASYNC)
                .setPassTicket(passTicket)
                .setBaseRequestBody(new BaseRequestBody(uin, sid, skey))
                .build());
    }

    /**
     * 发送动态表情消息
     *
     * @param msg 需要发送的动态表情消息
     * @return 发送的结果
     */
    public WebWxSendMsgResp webWxSendEmoticon(Msg msg) {
        String id = String.valueOf(BaseUtil.getEpochSecond() * 10000);
        msg.setClientMsgId(id);
        msg.setLocalId(id);
        return WeChatHttpClient.post(new WebWxSendMsgFileReq(String.format(WEB_WX_SEND_EMOTICON, host))
                .setMsg(msg)
                .setFun(WXQueryValue.SYS)
                .setBaseRequestBody(new BaseRequestBody(uin, sid, skey))
                .build());
    }

    /**
     * 发送文件附件消息
     *
     * @param msg 需要发送的文件附件消息
     * @return 发送的结果
     */
    public WebWxSendMsgResp webWxSendAppMsg(Msg msg) {
        String id = String.valueOf(BaseUtil.getEpochSecond() * 10000);
        msg.setClientMsgId(id);
        msg.setLocalId(id);
        return WeChatHttpClient.post(new WebWxSendMsgFileReq(String.format(WEB_WX_SEND_APP_MSG, host))
                .setMsg(msg)
                .setFun(WXQueryValue.ASYNC)
                .setBaseRequestBody(new BaseRequestBody(uin, sid, skey))
                .build());
    }

    /**
     * 撤回消息
     *
     * @param clientMsgId 本地消息id
     * @param serverMsgId 服务端消息id
     * @param userName    消息接收人userName
     * @return 撤回结果
     */
    public WebWxRevokeMsgResp webWxRevokeMsg(long clientMsgId, long serverMsgId, String userName) {
        return WeChatHttpClient.post(new WebWxRevokeMsgReq(String.format(WEB_WX_REVOKE_MSG, host))
                .setClientMsgId(String.valueOf(clientMsgId))
                .setSvrMsgId(String.valueOf(serverMsgId))
                .setToUserName(userName)
                .setBaseRequestBody(new BaseRequestBody(uin, sid, skey))
                .build());
    }


    /**
     * 根据MsgId获取那条消息的图片
     *
     * @param msgId 消息ID
     * @param type  图片的类型，slave：小图，big：大图，不传该参数则默认获取大图
     * @return 获取到的图片文件
     */
    public File webWxGetMsgImg(long msgId, String type) throws IOException {
        WebWxGetMsgImgReq webWxGetMsgImgReq = new WebWxGetMsgImgReq(String.format(WEB_WX_GET_MSG_IMG, host))
                .setMsgId(msgId)
                .setSKey(skey)
                .setType(type)
                .setPassTicket(passTicket)
                .build();
        WeChatHttpClient.get(webWxGetMsgImgReq);
        String path = folder.getAbsolutePath() + File.separator + String.format("image-%s-%s", type, msgId);
        File oldFile = new File(path);
        FileUtils.copyInputStreamToFile(webWxGetMsgImgReq.getInputStream(), oldFile);
        File newFile = new File(path + ImageTypeUtils.typeOf(path));
        FileUtils.moveFile(oldFile, newFile);
        return newFile;
    }

    /**
     * 根据MsgId获取那条消息的语音
     *
     * @param msgId 消息ID
     * @return 获取到的语音文件
     */
    public File webWxGetVoice(long msgId) throws IOException {
        WebWxGetVoiceReq webWxGetVoiceReq = new WebWxGetVoiceReq(String.format(WEB_WX_GET_VOICE, host))
                .setMsgId(msgId)
                .setSKey(skey)
                .setPassTicket(passTicket)
                .build();
        WeChatHttpClient.get(webWxGetVoiceReq);
        String voiceFilepath = folder.getAbsolutePath() + File.separator + String.format("voice-%s.mp3", msgId);
        File voiceFile = new File(voiceFilepath);
        FileUtils.copyInputStreamToFile(webWxGetVoiceReq.getInputStream(), voiceFile);
        return voiceFile;
    }

    /**
     * 根据MsgId获取那条消息的视频
     *
     * @param msgId 消息ID
     * @return 获取到的视频文件
     */
    public File webWxGetVideo(long msgId) throws IOException {
        WebWxGetVideoReq wxGetVideoReq = new WebWxGetVideoReq(String.format(WEB_WX_GET_VIDEO, host))
                .setMsgId(msgId)
                .setSKey(skey)
                .setPassTicket(passTicket)
                .build();
        WeChatHttpClient.get(wxGetVideoReq);
        String videoFilepath = folder.getAbsolutePath() + File.separator + String.format("video-%d.mp4", msgId);
        File videoFile = new File(videoFilepath);
        FileUtils.copyInputStreamToFile(wxGetVideoReq.getInputStream(), videoFile);
        return videoFile;
    }

    /**
     * 根据MsgId获取那条消息的附件文件
     *
     * @param msgId    消息ID
     * @param filename 转码后的文件名
     * @param mediaId  文件id
     * @param sender   文件发送者
     * @return 获取到的附件文件
     */
    public File webWxGetMedia(long msgId, String filename, String mediaId, String sender) throws IOException {
        WebWxGetMediaReq webWxGetVideoReq = new WebWxGetMediaReq(String.format(WEB_WX_GET_MEDIA, host))
                .setEncryFileName(filename)
                .setFromUser(uin)
                .setMediaId(mediaId)
                .setPassTicket(passTicket)
                .setSender(sender)
                .setWebWxDataTicket(dataTicket)
                .build();
        WeChatHttpClient.get(webWxGetVideoReq);
        String suffix = filename.lastIndexOf('.') > 0 ? filename.substring(filename.lastIndexOf('.')) : "";
        String mediaFilepath = folder.getAbsolutePath() + File.separator + String.format("media-%d%s", msgId, suffix);
        File mediaFile = new File(mediaFilepath);
        FileUtils.copyInputStreamToFile(webWxGetVideoReq.getInputStream(), mediaFile);
        return mediaFile;
    }

    /**
     * 发送或同意好友请求，现在发送好友请求功能已经失效
     *
     * @param opCode        操作，2：发送好友申请（已失效），3：同意好友申请
     * @param userName      目标用户的UserName
     * @param verifyTicket  验证票据
     * @param verifyContent 验证消息
     * @return 发送的结果
     */
    public BaseResponseBaseBody webWxVerifyUser(int opCode, String userName, String verifyTicket, String verifyContent) {
        return WeChatHttpClient.post(new WebWxVerifyUserReq(String.format(WEB_WX_VERIFY_USER, host))
                .setOpCode(opCode)
                .setUserName(userName)
                .setVerifyTicket(verifyTicket)
                .setVerifyContent(verifyContent)
                .setPassTicket(passTicket)
                .setBaseRequestBody(new BaseRequestBody(uin, sid, skey))
                .build());
    }

    /**
     * 修改用户备注
     *
     * @param cmdId      指令id
     * @param op         操作码
     * @param userName   目标用户的UserName
     * @param remarkName 备注名称
     * @return 修改备注的结果
     */
    public BaseResponseBaseBody webWxOpLog(int cmdId, int op, String userName, String remarkName) {
        return WeChatHttpClient.post(new WebWxOpLogReq(String.format(WEB_WX_OP_LOG, host))
                .setCmdId(cmdId)
                .setOp(op)
                .setUserName(userName)
                .setRemarkName(remarkName)
                .setPassTicket(passTicket)
                .setBaseRequestBody(new BaseRequestBody(uin, sid, skey))
                .build());
    }

    /**
     * 添加或移除聊天室成员
     *
     * @param chatRoom   聊天室的UserName
     * @param fun        addmember：添加成员，delmember：移除成员
     * @param name       聊天室名称
     * @param memberList 成员列表
     * @return 添加或移除的结果
     */
    public WebWxUpdateChatRoomResp webWxUpdateChatRoom(String chatRoom, String fun, String name, List<String> memberList) {
        return WeChatHttpClient.post(new WebWxUpdateChatRoomReq(String.format(WEB_WX_UPDATE_CHAT_ROOM, host))
                .setChatRoom(chatRoom)
                .setFun(fun)
                .setName(name)
                .setMemberList(memberList)
                .setPassTicket(passTicket)
                .setBaseRequestBody(new BaseRequestBody(uin, sid, skey))
                .build());
    }

    /**
     * 文件秒传接口，传输大于25M的文件会先进行检查服务器是否已经存在该文件
     *
     * @param file         需要传输的文件
     * @param fromUserName 消息的发送方UserName
     * @param toUserName   消息的接收方UserName
     * @return 秒传结果，
     */
    public WebWxCheckUploadResp  webWxCheckUpload(File file, String fromUserName, String toUserName) {
        return WeChatHttpClient.post(new WebWxCheckUploadReq(String.format(WEB_WX_CHECK_UPLOAD, host))
                        .setFile(file)
                        .setFromUserName(fromUserName)
                        .setToUserName(toUserName)
                        .setBaseRequestBody(new BaseRequestBody(uin, sid, skey))
                .build());
    }

    ///**
    // * 上传资源文件，超过1M的文件会被分片上传，每片512K
    // *
    // * @param fromUserName 消息的发送方UserName
    // * @param toUserName   消息的接收方UserName
    // * @param file         要上传的资源文件
    // * @return 上传结果，包含了MediaId
    // * @throws IOException 文件IO异常
    // */
    //RspUploadMedia webwxuploadmedia(String fromUserName, String toUserName, File file, String aesKey, String signature) throws IOException {
    //    int fileId = this.file++;
    //    String fileName = file.getName();
    //    String fileMime = Files.probeContentType(Paths.get(file.getAbsolutePath()));
    //    String fileMd5 = XTools.md5(file);
    //    String fileType = WeChatTools.fileType(file);
    //    long fileLength = file.length();
    //    long clientMediaId = ReqUploadMedia.clientMediaId();
    //    if (file.length() < 1024L * 1024L) {
    //        XRequest request = XRequest.POST(String.format("https://file.%s/cgi-bin/mmwebwx-bin/webwxuploadmedia", host));
    //        request.query("f", "json");
    //        request.content("id", String.format("WU_FILE_%d", fileId));
    //        request.content("name", fileName);
    //        request.content("type", fileMime);
    //        request.content("lastModifiedDate", new Date(file.lastModified()));
    //        request.content("size", fileLength);
    //        request.content("mediatype", fileType);
    //        request.content("uploadmediarequest", GSON.toJson(new ReqUploadMedia(new BaseRequest<W>(uin, sid, skey), clientMediaId, 2, fileLength, 0, fileLength, fileMd5, aesKey, signature, fromUserName, toUserName)));
    //        request.content("webwx_data_ticket", dataTicket);
    //        request.content("pass_ticket", StringUtils.isNotEmpty(passticket) ? "undefined" : passticket);
    //        request.content("filename", file);
    //        return GSON.fromJson(XTools.http(httpExecutor, request).string(), RspUploadMedia.class);
    //    } else {
    //        RspUploadMedia rspUploadMedia = null;
    //        byte[] sliceBuffer = new byte[512 * 1024];
    //        try (BufferedInputStream bfinStream = new BufferedInputStream(new FileInputStream(file))) {
    //            for (long sliceIndex = 0, sliceCount = (long) Math.ceil(file.length() / 512D / 1024D); sliceIndex < sliceCount; sliceIndex++) {
    //                XRequest request = XRequest.POST(String.format("https://file.%s/cgi-bin/mmwebwx-bin/webwxuploadmedia", host));
    //                request.query("f", "json");
    //                request.content("id", String.format("WU_FILE_%d", fileId));
    //                request.content("name", fileName);
    //                request.content("type", fileMime);
    //                request.content("lastModifiedDate", new Date(file.lastModified()));
    //                request.content("size", fileLength);
    //                request.content("chunks", sliceCount);
    //                request.content("chunk", sliceIndex);
    //                request.content("mediatype", fileType);
    //                request.content("uploadmediarequest", GSON.toJson(new ReqUploadMedia(new BaseRequest<W>(uin, sid, skey), clientMediaId, 2, fileLength, 0, fileLength, fileMd5, aesKey, signature, fromUserName, toUserName)));
    //                request.content("webwx_data_ticket", dataTicket);
    //                request.content("pass_ticket", StringUtils.isNotEmpty(passticket) ? "undefined" : passticket);
    //                int readCount;
    //                WeChatTools.Slice slice = new WeChatTools.Slice("filename", fileName, fileMime, sliceBuffer, 0);
    //                while ((readCount = bfinStream.read(sliceBuffer, slice.count, sliceBuffer.length - slice.count)) > 0) {
    //                    slice.count += readCount;
    //                    if (slice.count >= sliceBuffer.length) {
    //                        break;
    //                    }
    //                }
    //                request.content("filename", slice);
    //                rspUploadMedia = GSON.fromJson(XTools.http(httpExecutor, request).string(), RspUploadMedia.class);
    //            }
    //        }
    //        return rspUploadMedia;
    //    }
    //}

    /**
     * 退出登录接口
     */
    public void webWxLogout() {
        //WeChatHttpClient.post()
        //XRequest request = XRequest.POST(String.format("https://%s/cgi-bin/mmwebwx-bin/webwxlogout", host));
        //request.query("redirect", 1);
        //request.query("type", 0);
        //request.query("skey", this.skey);
        //request.content("sid", this.sid);
        //request.content("uin", this.uin);
        //XTools.http(httpExecutor, request).string();
    }
}