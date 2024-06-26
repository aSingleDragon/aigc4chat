package pers.hll.aigc4chat.server.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pers.hll.aigc4chat.base.util.XmlUtil;
import pers.hll.aigc4chat.entity.wechat.message.OriContent;
import pers.hll.aigc4chat.protocol.wechat.response.webwxsync.AddMsg;
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

    private final IWeChatApiService weChatApiService;

    @Override
    public void handleTextMessage(AddMsg addMsg) {
        weChatApiService.sendTextMessage(addMsg.getContent(), addMsg.getFromUserName());
    }

    @Override
    public void handleLocationMessage(AddMsg addMsg) {
        weChatApiService.sendLocationMessage(
                XmlUtil.xmlStrToObject(addMsg.getOriContent(), OriContent.class),
                addMsg.getToUserName());
    }
}
