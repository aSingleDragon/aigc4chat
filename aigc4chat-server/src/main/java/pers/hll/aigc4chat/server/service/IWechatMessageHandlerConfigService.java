package pers.hll.aigc4chat.server.service;

import pers.hll.aigc4chat.server.entity.WechatMessageHandlerConfig;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  消息处理器服务接口
 * </p>
 *
 * @author hll
 * @since 2024-04-22
 */
public interface IWechatMessageHandlerConfigService extends IService<WechatMessageHandlerConfig> {

    String getHandlerName(String userName);

    List<String> listHandlerName();
}
