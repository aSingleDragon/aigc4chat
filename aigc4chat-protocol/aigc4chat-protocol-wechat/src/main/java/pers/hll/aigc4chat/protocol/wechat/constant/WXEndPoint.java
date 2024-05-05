package pers.hll.aigc4chat.protocol.wechat.constant;

/**
 * 微信接口
 *
 * @author hll
 * @since 2024/03/10
 */
public interface WXEndPoint {

    /**
     * 获取登录二维码地址
     */
    String JS_LOGIN = "https://login.weixin.qq.com/jslogin";

    /**
     * 获取登录二维码地址 redirect_uri
     */
    String WEB_WX_NEW_LOGIN_PAGE = "https://wx.qq.com/cgi-bin/mmwebwx-bin/webwxnewloginpage?mod=desktop";

    /**
     * 登录二维码地址
     * 可以用这个地址生成在线的二维码 <a href="https://wechaty.js.org/qrcode/">在线二维码</a>
     */
    String QR_CODE = "https://login.weixin.qq.com/l/%s";

    /**
     * 登录请求地址
     */
    String LOGIN = "https://login.wx.qq.com/cgi-bin/mmwebwx-bin/login";

    /**
     * 初始化请求地址 获取自身信息，好友列表，活跃群等
     */
    String WEB_WX_INIT ="https://%s/cgi-bin/mmwebwx-bin/webwxinit";

    /**
     * 状态更新接口，登录登出，消息已读
     */
    String WEB_WX_STATUS_NOTIFY = "https://%s/cgi-bin/mmwebwx-bin/webwxstatusnotify";

    /**
     * 获取联系人信息
     */
    String WEB_WX_GET_GET_CONTACT = "https://%s/cgi-bin/mmwebwx-bin/webwxgetcontact";

    /**
     * 同步检查接口，需要无限循环请求该接口，如果有消息要同步，则该接口立即返回并携带参数，否则将在60秒后返回
     */
    String SYNC_CHECK = "https://webpush.%s/cgi-bin/mmwebwx-bin/synccheck";

    /**
     * 同步接口，将服务端数据同步到本地
     */
    String WEB_WX_SYNC = "https://%s/cgi-bin/mmwebwx-bin/webwxsync";

    /**
     * 批量获取联系人详细信息
     */
    String WEB_WX_BATCH_GET_CONTACT = "https://%s/cgi-bin/mmwebwx-bin/webwxbatchgetcontact";

    /**
     * 根据MsgId获取那条消息的图片
     */
    String WEB_WX_GET_MSG_IMG = "https://%s/cgi-bin/mmwebwx-bin/webwxgetmsgimg";

    /**
     * 发送消息接口
     */
    String WEB_WX_SEND_MSG = "https://%s/cgi-bin/mmwebwx-bin/webwxsendmsg";

    /**
     * 发送图片消息
     */
    String WEB_WX_SEND_MSG_IMG = "https://%s/cgi-bin/mmwebwx-bin/webwxsendmsgimg";

    /**
     * 发送视频消息
     */
    String WEB_WX_SEND_VIDEO_MSG = "https://%s/cgi-bin/mmwebwx-bin/webwxsendvideomsg";

    /**
     * 发送表情包
     */
    String WEB_WX_SEND_EMOTICON = "https://%s/cgi-bin/mmwebwx-bin/webwxsendemoticon";

    /**
     * 发送文件附件消息
     */
    String WEB_WX_SEND_APP_MSG = "https://%s/cgi-bin/mmwebwx-bin/webwxsendappmsg";

    /**
     * 撤回消息
     */
    String WEB_WX_REVOKE_MSG = "https://%s/cgi-bin/mmwebwx-bin/webwxrevokemsg";

    /**
     * 根据MsgId获取那条消息的语音
     */
    String WEB_WX_GET_VOICE = "https://%s/cgi-bin/mmwebwx-bin/webwxgetvoice";

    /**
     * 根据MsgId获取那条消息的视频
     */
    String WEB_WX_GET_VIDEO = "https://%s/cgi-bin/mmwebwx-bin/webwxgetvideo";

    /**
     * 根据MsgId获取那条消息的附件文件
     */
    String WEB_WX_GET_MEDIA = "https://file.%s/cgi-bin/mmwebwx-bin/webwxgetmedia";

    /**
     * 发送或同意好友请求，现在发送好友请求功能已经失效
     */
    String WEB_WX_VERIFY_USER =  "https://%s/cgi-bin/mmwebwx-bin/webwxverifyuser";

    /**
     * 修改用户备注
     */
    String WEB_WX_OP_LOG = "https://%s/cgi-bin/mmwebwx-bin/webwxoplog";

    /**
     * 添加或移除聊天室成员
     */
    String WEB_WX_UPDATE_CHAT_ROOM = "https://%s/cgi-bin/mmwebwx-bin/webwxupdatechatroom";

    /**
     * 文件秒传接口，传输大于25M的文件会先进行检查服务器是否已经存在该文件
     */
    String WEB_WX_CHECK_UPLOAD = "https://%s/cgi-bin/mmwebwx-bin/webwxcheckupload";

    /**
     * 文件上传
     */
    String WEB_WX_UPLOAD_MEDIA = "https://file.%s/cgi-bin/mmwebwx-bin/webwxuploadmedia";

    /**
     * 登出
     */
    String WEB_WX_LOGOUT = "https://%s/cgi-bin/mmwebwx-bin/webwxlogout";
}
