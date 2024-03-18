package pers.hll.aigc4chat.common.protocol.wechat.protocol.request;

import lombok.EqualsAndHashCode;
import pers.hll.aigc4chat.common.base.constant.ContentType;
import pers.hll.aigc4chat.common.base.util.BaseUtil;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.constant.DefaultConfig;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.constant.WXHeaderKey;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.constant.WXQueryKey;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.request.body.*;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.response.WebWxSendMsgResp;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.response.WebWxUpdateChatRoomResp;

import java.util.List;
import java.util.Map;

/**
 * @author hll
 * @since 2023/03/11
 */
@EqualsAndHashCode(callSuper = true)
public class WebWxUpdateChatRoomReq extends BasePostRequest<WebWxUpdateChatRoomReq, WebWxUpdateChatRoomResp> {

    private String chatRoom;

    private String fun;

    private String name;

    private List<String> memberList;

    private String passTicket;

    public WebWxUpdateChatRoomReq(String uri) {
        super(uri);
    }

    public WebWxUpdateChatRoomReq setChatRoom(String chatRoom) {
        this.chatRoom = chatRoom;
        return this;
    }

    public WebWxUpdateChatRoomReq setFun(String fun) {
        this.fun = fun;
        return this;
    }

    public WebWxUpdateChatRoomReq setName(String name) {
        this.name = name;
        return this;
    }

    public WebWxUpdateChatRoomReq setMemberList(List<String> memberList) {
        this.memberList = memberList;
        return this;
    }

    public WebWxUpdateChatRoomReq setPassTicket(String passTicket) {
        this.passTicket = passTicket;
        return this;
    }

    @Override
    public WebWxUpdateChatRoomReq build() {

        Map<String, String> headerMap = getHeaderMap();
        headerMap.put(WXHeaderKey.USER_AGENT, DefaultConfig.USER_AGENT);
        headerMap.put(WXHeaderKey.CONTENT_TYPE, ContentType.JSON);

        Map<String, Object> requestParamMap = getRequestParamMap();
        requestParamMap.put(WXQueryKey.PASS_TICKET, passTicket);
        requestParamMap.put(WXQueryKey.FUN, fun);

        return this;
    }

    @Override
    public String buildRequestBody() {
        String members = String.join(",", memberList);
        WebWxUpdateChatRoomReqBody webWxUpdateChatRoomReqBody = new WebWxUpdateChatRoomReqBody()
                .setChatRoomName(chatRoom)
                .setNewTopic(name);
        switch (fun) {
            case "addmember":
                webWxUpdateChatRoomReqBody.setAddMemberList(members);
                break;
            case "delmember":
                webWxUpdateChatRoomReqBody.setDelMemberList(members);
                break;
            default:
                break;
        }
        return BaseUtil.GSON.toJson(webWxUpdateChatRoomReqBody.setBaseRequestBody(this.getBaseRequestBody()));
    }

    @Override
    public WebWxUpdateChatRoomResp convertRespBodyToObj(String strEntity) {
        return BaseUtil.GSON.fromJson(strEntity, WebWxUpdateChatRoomResp.class);
    }

    @Override
    public WebWxUpdateChatRoomReq setBaseRequestBody(BaseRequestBody baseRequestBody) {
        this.baseRequestBody = baseRequestBody;
        return this;
    }
}
