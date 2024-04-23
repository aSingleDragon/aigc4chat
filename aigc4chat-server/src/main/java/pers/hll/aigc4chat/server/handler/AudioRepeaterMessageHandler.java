package pers.hll.aigc4chat.server.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pers.hll.aigc4chat.common.base.util.XmlUtil;
import pers.hll.aigc4chat.common.entity.wechat.message.OriContent;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.response.webwxsync.AddMsg;
import pers.hll.aigc4chat.server.service.IWeChatApiService;

/**
 * 消息处理器
 *
 * @author hll
 * @since 2024/04/21
 */
@RequiredArgsConstructor
@Component(MessageHandlerName.AUDIO_REPEATER_MESSAGE_HANDLER)
public class AudioRepeaterMessageHandler implements MessageHandler {

    private final IWeChatApiService websChatApiService;

    @Override
    public void handleTextMessage(AddMsg addMsg) {
        websChatApiService.sendTextMessage(addMsg.getContent(), addMsg.getFromUserName());
    }

    @Override
    public void handleLocationMessage(AddMsg addMsg) {
        websChatApiService.sendLocationMessage(
                XmlUtil.xmlStrToObject(addMsg.getOriContent(), OriContent.class),
                addMsg.getToUserName());
    }
}
