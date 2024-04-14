package pers.hll.aigc4chat.server.service.impl;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.ContentType;
import org.springframework.stereotype.Service;
import pers.hll.aigc4chat.common.base.util.BaseUtil;
import pers.hll.aigc4chat.common.base.util.ImgTypeUtil;
import pers.hll.aigc4chat.common.base.util.QRCodeUtil;
import pers.hll.aigc4chat.common.base.util.XmlUtil;
import pers.hll.aigc4chat.common.entity.wechat.message.AppMsg;
import pers.hll.aigc4chat.common.entity.wechat.message.OriContent;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.WeChatHttpClient;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.constant.MsgType;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.constant.WXQueryValue;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.request.*;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.request.body.Contact;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.request.body.Msg;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.request.form.FormFile;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.request.form.UploadMediaRequest;
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

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static pers.hll.aigc4chat.common.protocol.wechat.protocol.constant.WXEndPoint.*;

/**
 * 微信接口服务实现类
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
    public void jsLogin(HttpServletResponse response) {
        JsLoginResp jsLoginResp = WeChatHttpClient.get(new JsLoginReq(JS_LOGIN).build());
        if (200 != jsLoginResp.getCode()) {
            throw new IllegalStateException("获取登录二维码出错");
        } else {
            String uuid = jsLoginResp.getUuid();
            WeChatRequestCache.getNeededInfo().setUuid(uuid);
            String qrCodeUri = String.format(QR_CODE, uuid);
            QRCodeUtil.write2Response(qrCodeUri, response);
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
    public void sendLocationMessage(OriContent oriContent, String toUserName) {
        Msg msg = new Msg(
                MsgType.LOCATION,
                null,
                0,
                XmlUtil.objectToXmlStr(oriContent, OriContent.class),
                null,
                getFromUserName(),
                toUserName);
        setClientIdAndLocalId(msg);
        WeChatHttpClient.post(new WebWxSendMsgReq(String.format(WEB_WX_SEND_MSG, getHost()))
                .setMsg(msg)
                .setBaseRequestBody(WeChatRequestCache.getBaseRequestBody())
                .build());
    }

    @Override
    public void sendTextMessage(String text, String toUserName) {
        Msg msg = new Msg(
                MsgType.TEXT,
                null,
                0,
                text,
                null,
                getFromUserName(),
                toUserName);
        setClientIdAndLocalId(msg);
        WeChatHttpClient.post(new WebWxSendMsgReq(String.format(WEB_WX_SEND_MSG, getHost()))
                .setMsg(msg)
                .setBaseRequestBody(WeChatRequestCache.getBaseRequestBody())
                .build());
    }

    @Override
    public void sendVoiceMessage(String voiceFilePath, String toUserName) {
        WebWxUploadMediaResp resp =
                webWxUploadMedia(voiceFilePath, null, null, toUserName);
        Msg msg = Msg.builder()
                .type(MsgType.VOICE)
                .mediaId(resp.getMediaId())
                .emojiFlag(0)
                .fromUserName(getFromUserName()).toUserName(toUserName)
                .build();
        setClientIdAndLocalId(msg);
        WeChatHttpClient.post(new WebWxSendMsgReq(String.format(WEB_WX_SEND_MSG, getHost()))
                .setMsg(msg)
                .setBaseRequestBody(WeChatRequestCache.getBaseRequestBody())
                .build());
    }

    @Override
    public void sendFileMessage(String filePath, String toUserName) {
        File file = new File(filePath);
        String suffix = ImgTypeUtil.fileSuffix(file);
        if ("mp4".equals(suffix) && filePath.length() >= 20L * 1024L * 1024L) {
            log.warn("向[{}]发送的视频文件大于20M，无法发送", toUserName);
        } else {
            String mediaId = null, aesKey = null, signature = null;
            // 如果文件大于25M，则检查文件是否已经在微信服务器上
            if (filePath.length() >= 25L * 1024L * 1024L) {
                WebWxCheckUploadResp rspCheckUpload = webWxCheckUpload(filePath, toUserName);
                mediaId = rspCheckUpload.getMediaId();
                aesKey = rspCheckUpload.getAseKey();
                signature = rspCheckUpload.getSignature();
            }
            // 如果文件不在微信服务器上，则上传文件
            if (StringUtils.isEmpty(mediaId)) {
                WebWxUploadMediaResp webWxUploadMediaResp = webWxUploadMedia(filePath, aesKey, signature, toUserName);
                mediaId = webWxUploadMediaResp.getMediaId();
            }
            if (StringUtils.isNotEmpty(mediaId)) {
                switch (ImgTypeUtil.fileType(file)) {
                    case "pic": {
                        webWxSendMsgImg(mediaId, signature, toUserName);
                        break;
                    }
                    case "video": {
                        webWxSendVideoMsg(mediaId, signature, toUserName);
                        break;
                    }
                    default:
                        if ("gif".equals(suffix)) {
                            webWxSendEmoticon(mediaId, signature, toUserName);
                        } else {
                            webWxSendAppMsg(mediaId, file.getName(), suffix, file.length(), signature, toUserName);
                        }
                }
            } else {
                log.error("向({})发送的文件发送失败", toUserName);
            }
        }
    }

    @Override
    public WebWxUploadMediaResp webWxUploadMedia(String filePath, String aesKey, String signature, String toUserName) {
        File file = new File(filePath);
        String fileName = file.getName();
        String fileMime = null;
        try {
            fileMime = Files.probeContentType(Paths.get(file.getAbsolutePath()));
        } catch (IOException e) {
            log.error("文件类型获取失败", e);
            throw new IllegalArgumentException(e);
        }
        String fileMd5 = BaseUtil.md5(file);
        String fileType = ImgTypeUtil.fileType(file);
        long fileLength = file.length();
        long clientMediaId = BaseUtil.getEpochSecond() * 10;
        UploadMediaRequest uploadMediaRequest = new UploadMediaRequest(
                2, clientMediaId, fileLength, 4, fileLength, 4, getFromUserName(),
                toUserName, fileMd5, aesKey, signature);
        uploadMediaRequest.setBaseRequestBody(WeChatRequestCache.getBaseRequestBody());
        String uploadMediaRequestJson = BaseUtil.GSON.toJson(uploadMediaRequest);
        // 文件大小 < 1MB 直接上传
        if (file.length() < 1024L * 1024L) {
            byte[] fileContentInBytes = new byte[0];
            try {
                fileContentInBytes = FileUtils.readFileToByteArray(file);
            } catch (IOException e) {
                log.error("文件读取失败", e);
            }
            return WeChatHttpClient.post(new WebWxUploadMediaReq(String.format(WEB_WX_UPLOAD_MEDIA, getHost()))
                    //.setId(String.format("WU_FILE_%d", fileId++))
                    .setId("WU_FILE_0")
                    .setName(fileName)
                    .setType(fileMime)
                    .setLastModifiedDate(BaseUtil.getWechatTime(file.lastModified()))
                    .setSize(fileLength)
                    .setMediaType(fileType)
                    .setUploadMediaRequest(uploadMediaRequestJson)
                    .setWebWxDataTicket(WeChatRequestCache.getNeededInfo().getWebWxDataTicket())
                    .setPassTicket(StringUtils.isEmpty(WeChatRequestCache.getPassTicket())
                            ? "undefined"
                            : WeChatRequestCache.getPassTicket())
                    .setFormFile(new FormFile(
                            "fileName", ContentType.APPLICATION_OCTET_STREAM, fileName, fileContentInBytes))
                    .build());
        } else {
            // 分片上传 每片512K
            WebWxUploadMediaResp webWxUploadMediaResp = null;
            final int halfMb = 512 * 1024;
            byte[] sliceBuffer = new byte[halfMb];
            try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file))) {
                for (long sliceIndex = 0, sliceCount = (long) Math.ceil((double) file.length() / halfMb); sliceIndex < sliceCount; sliceIndex++) {
                    int readCount;
                    while ((readCount = bufferedInputStream.read(sliceBuffer)) != -1) {
                        log.info("sliceIndex: {}, readCount: {}", sliceIndex, readCount);
                    }
                    webWxUploadMediaResp = WeChatHttpClient.post(new WebWxUploadMediaReq(String.format(WEB_WX_UPLOAD_MEDIA, getHost()))
                            //.setId(String.format("WU_FILE_%d", fileId++))
                            .setId(String.format("WU_FILE_%d", 0))
                            .setName(fileName)
                            .setType(fileMime)
                            .setLastModifiedDate(BaseUtil.getWechatTime(file.lastModified()))
                            .setSize(fileLength)
                            .setChunks(sliceCount)
                            .setChunk(sliceIndex)
                            .setMediaType(fileType)
                            .setUploadMediaRequest(uploadMediaRequestJson)
                            .setWebWxDataTicket(WeChatRequestCache.getNeededInfo().getWebWxDataTicket())
                            .setPassTicket(StringUtils.isEmpty(WeChatRequestCache.getPassTicket())
                                    ? "undefined"
                                    : WeChatRequestCache.getPassTicket())
                            .setFormFile(new FormFile("fileName", ContentType.APPLICATION_OCTET_STREAM, fileName, sliceBuffer))
                            .build());
                    log.info("[{}]第{}次分片上传:", fileName, sliceIndex);
                }
            } catch (FileNotFoundException e) {
                log.error("文件不存在: ", e);
            } catch (IOException e) {
                log.error("文件读取失败: ", e);
            }
            return webWxUploadMediaResp;
        }
    }

    @Override
    public WebWxSendMsgResp webWxSendMsgImg(String mediaId, String signature, String toUserName) {
        Msg msg = new Msg(MsgType.IMAGE, mediaId, null, "", signature, getFromUserName(), toUserName);
        setClientIdAndLocalId(msg);
        return WeChatHttpClient.post(new WebWxSendMsgFileReq(String.format(WEB_WX_SEND_MSG_IMG, getHost()))
                .setMsg(msg)
                .setFun(WXQueryValue.ASYNC)
                .setBaseRequestBody(WeChatRequestCache.getBaseRequestBody())
                .build());
    }


    @Override
    public WebWxSendMsgResp webWxSendVideoMsg(String mediaId, String signature, String toUserName) {
        Msg msg = new Msg(MsgType.VIDEO, mediaId, null, "", signature, getFromUserName(), toUserName);
        setClientIdAndLocalId(msg);
        return WeChatHttpClient.post(new WebWxSendMsgFileReq(String.format(WEB_WX_SEND_VIDEO_MSG, getHost()))
                .setMsg(msg)
                .setFun(WXQueryValue.ASYNC)
                .setPassTicket(WeChatRequestCache.getPassTicket())
                .setBaseRequestBody(WeChatRequestCache.getBaseRequestBody())
                .build());
    }

    @Override
    public WebWxSendMsgResp webWxSendEmoticon(String mediaId, String signature, String toUserName) {
        Msg msg = new Msg(MsgType.EMOJI, mediaId, 2, "", signature, getFromUserName(), toUserName);
        setClientIdAndLocalId(msg);
        return WeChatHttpClient.post(new WebWxSendMsgFileReq(String.format(WEB_WX_SEND_EMOTICON, getHost()))
                .setMsg(msg)
                .setFun(WXQueryValue.SYS)
                .setBaseRequestBody(WeChatRequestCache.getBaseRequestBody())
                .build());
    }

    @Override
    public WebWxSendMsgResp webWxSendAppMsg(String mediaId, String fileName, String fileExt, long totalLen,
                                            String signature, String toUserName) {
        AppMsg appMsg = new AppMsg();
        appMsg.setTitle(fileName);
        appMsg.setAppAttach(new AppMsg.AppAttach(totalLen, mediaId, fileExt));
        Msg msg = new Msg(
                6,
                null,
                null,
                XmlUtil.objectToXmlStr(appMsg, AppMsg.class),
                signature,
                getFromUserName(), toUserName);
        setClientIdAndLocalId(msg);
        return WeChatHttpClient.post(new WebWxSendMsgFileReq(String.format(WEB_WX_SEND_APP_MSG, getHost()))
                .setMsg(msg)
                .setFun(WXQueryValue.ASYNC)
                .setBaseRequestBody(WeChatRequestCache.getBaseRequestBody())
                .build());
    }

    @Override
    public WebWxCheckUploadResp webWxCheckUpload(String filePath, String toUserName) {
        return WeChatHttpClient.post(new WebWxCheckUploadReq(String.format(WEB_WX_CHECK_UPLOAD, getHost()))
                .setFilePath(filePath)
                .setFromUserName(getFromUserName())
                .setToUserName(toUserName)
                .setBaseRequestBody(WeChatRequestCache.getBaseRequestBody())
                .build());
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

    private void setClientIdAndLocalId(Msg msg) {
        String id = String.valueOf(BaseUtil.getEpochSecond() * 10000);
        msg.setClientMsgId(id);
        msg.setLocalId(id);
    }

    private String getFromUserName() {
        return weChatUserService.selectMe().getUserName();
    }
}
