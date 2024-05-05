package pers.hll.aigc4chat.server.service;

import jakarta.servlet.http.HttpServletResponse;
import pers.hll.aigc4chat.entity.wechat.message.OriContent;
import pers.hll.aigc4chat.protocol.wechat.request.body.Contact;
import pers.hll.aigc4chat.protocol.wechat.response.*;

import java.util.List;

/**
 * 微信接口
 *
 * @author hll
 * @since 2023/04/06
 */
public interface IWeChatApiService {

    void jsLogin(HttpServletResponse response);

    LoginResp login();

    void webWxGetContact();

    void webWxBatchGetContact(List<Contact> contactList);

    WebWxStatusNotifyResp webWxStatusNotify(String userName, int notifyCode);

    WebWxInitResp webWxInit();

    SyncCheckResp syncCheck();

    WebWxSyncResp webWxSync();

    void webWxNewLoginPage(String url);

    void sendLocationMessage(OriContent oriContent, String toUserName);

    void sendTextMessage(String text, String toUserName);

    void sendVoiceMessage(String voiceFilePath, String toUserName);

    void sendFileMessage(String filePath, String toUserName);

    WebWxUploadMediaResp webWxUploadMedia(String filePath, String aesKey, String signature, String toUserName);

    /**
     * 文件秒传接口，传输大于25M的文件会先进行检查服务器是否已经存在该文件
     *
     * @param filePath   需要传输的文件路径
     * @param toUserName 消息的接收方UserName
     * @return 秒传结果，
     */

    WebWxCheckUploadResp webWxCheckUpload(String filePath, String toUserName);

    /**
     * 发送图片消息
     *
     * @return 发送的结果
     */
    WebWxSendMsgResp webWxSendMsgImg(String mediaId, String signature, String toUserName);

    /**
     * 发送视频消息
     *
     * @return 发送的结果
     */
    WebWxSendMsgResp webWxSendVideoMsg(String mediaId, String signature, String toUserName);

    /**
     * 发送动态表情消息
     *
     * @return 发送的结果
     */
    WebWxSendMsgResp webWxSendEmoticon(String mediaId, String signature, String toUserName);

    /**
     * 发送文件附件消息
     *
     * @return 发送的结果
     */

    WebWxSendMsgResp webWxSendAppMsg(String mediaId, String fileName, String fileExt, long totalLen,
                                     String signature, String toUserName);

    /**
     * 获取并保存不限数量和类型的联系人信息
     *
     * @param userNames 逗号分隔的联系人userName
     */
    void loadContacts(String userNames, boolean useCache);

    /**
     * 获取并保存不限数量和类型的联系人信息
     *
     * @param contacts 要获取的联系人的列表，数量和类型不限
     */
    void loadContacts(List<Contact> contacts, boolean useCache);

    /**
     * 微信连接是否活跃检查 如果已经断开或无法连接 抛出业务异常
     */
    void activeCheck();

    /**
     * 微信连接是否活跃检查 如果活跃则返回 ture 否则返回 false
     */
    boolean isActive();

    void logout();
}
