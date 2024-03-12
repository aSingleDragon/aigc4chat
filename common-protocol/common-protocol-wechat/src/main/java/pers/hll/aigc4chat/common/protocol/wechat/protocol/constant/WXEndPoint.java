package pers.hll.aigc4chat.common.protocol.wechat.protocol.constant;

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
    String NEW_LOGIN_PAGE = "https://wx.qq.com/cgi-bin/mmwebwx-bin/webwxnewloginpage?mod=desktop";

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
    String INIT ="https://%s/cgi-bin/mmwebwx-bin/webwxinit";

    /**
     * 批量获取联系人详细信息
     */
    String BATCH_GET_CONTACT = "https://%s/cgi-bin/mmwebwx-bin/webwxbatchgetcontact";

    /**
     * 状态更新接口，登录登出，消息已读
     */
    String STATUS_NOTIFY = "https://%s/cgi-bin/mmwebwx-bin/webwxstatusnotify";

    /**
     * 获取联系人信息
     */
    String GET_CONTACT = "https://%s/cgi-bin/mmwebwx-bin/webwxgetcontact";

    /**
     * 同步检查接口，需要无限循环请求该接口，如果有消息要同步，则该接口立即返回并携带参数，否则将在60秒后返回
     */
    String SYNC_CHECK = "https://webpush.%s/cgi-bin/mmwebwx-bin/synccheck";

    /**
     * 同步接口，将服务端数据同步到本地
     */
    String SYNC_DATA = "https://%s/cgi-bin/mmwebwx-bin/webwxsync";

    String APPID = "wx782c26e4c19acffb";
}
