package pers.hll.aigc4chat.server.converter;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.response.webwxsync.AddMsg;
import pers.hll.aigc4chat.server.entity.WeChatMessage;

import lombok.experimental.UtilityClass;
import java.util.Collections;
import java.util.List;

/**
* 实体类转换Converter
* <p> 代码由 ConverterUtil 生成
*
* @author hll
* @since 2024-04-06
*/
@UtilityClass
public class WeChatMessageConverter {

    public static WeChatMessage from(AddMsg addMsg) {
        if (addMsg == null) {
            return null;
        }
        WeChatMessage weChatMessage = new WeChatMessage();
        weChatMessage.setVoiceLength(addMsg.getVoiceLength());
        weChatMessage.setForwardFlag(addMsg.getForwardFlag());
        weChatMessage.setMsgId(addMsg.getMsgId());
        weChatMessage.setImgWidth(addMsg.getImgWidth());
        weChatMessage.setMediaId(addMsg.getMediaId());
        weChatMessage.setFileName(addMsg.getFileName());
        weChatMessage.setImgHeight(addMsg.getImgHeight());
        weChatMessage.setHasProductId(addMsg.getHasProductId());
        weChatMessage.setToUserName(addMsg.getToUserName());
        weChatMessage.setTicket(addMsg.getTicket());
        weChatMessage.setEncrFileName(addMsg.getEncrFileName());
        weChatMessage.setImgStatus(addMsg.getImgStatus());
        weChatMessage.setFromUserName(addMsg.getFromUserName());
        weChatMessage.setOriContent(addMsg.getOriContent());
        weChatMessage.setUrl(addMsg.getUrl());
        weChatMessage.setNewMsgId(addMsg.getNewMsgId());
        weChatMessage.setSubMsgType(addMsg.getSubMsgType());
        weChatMessage.setStatusNotifyUserName(addMsg.getStatusNotifyUserName());
        weChatMessage.setMsgType(addMsg.getMsgType());
        weChatMessage.setFileSize(addMsg.getFileSize());
        weChatMessage.setStatusNotifyCode(addMsg.getStatusNotifyCode());
        weChatMessage.setCreateTime(addMsg.getCreateTime());
        weChatMessage.setAppMsgType(addMsg.getAppMsgType());
        weChatMessage.setContent(addMsg.getContent());
        weChatMessage.setPlayLength(addMsg.getPlayLength());
        weChatMessage.setStatus(addMsg.getStatus());
        return weChatMessage;
    }

    public static List<WeChatMessage> from(List<AddMsg> addMsgList) {
        if (addMsgList == null) {
            return Collections.emptyList();
        }
        return addMsgList
                .stream()
                .map(WeChatMessageConverter::from)
                .toList();
    }
}