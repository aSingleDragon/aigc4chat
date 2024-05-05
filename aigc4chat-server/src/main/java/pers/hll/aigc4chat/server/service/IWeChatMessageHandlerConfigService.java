package pers.hll.aigc4chat.server.service;

import pers.hll.aigc4chat.server.entity.WeChatMessageHandlerConfig;
import com.baomidou.mybatisplus.extension.service.IService;
import pers.hll.aigc4chat.server.handler.MessageHandler;

import java.util.List;

/**
 * <p>
 *  消息处理器服务接口
 * </p>
 *
 * @author hll
 * @since 2024-04-22
 */
public interface IWeChatMessageHandlerConfigService extends IService<WeChatMessageHandlerConfig> {

    String getHandlerName(String userName);

    List<String> listHandlerName();

    MessageHandler getMessageHandler(String beanName);
}
