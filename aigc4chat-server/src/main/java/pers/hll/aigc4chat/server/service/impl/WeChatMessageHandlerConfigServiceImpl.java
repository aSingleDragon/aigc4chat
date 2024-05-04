package pers.hll.aigc4chat.server.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import pers.hll.aigc4chat.base.util.ConstantUtil;
import pers.hll.aigc4chat.server.entity.WeChatMessageHandlerConfig;
import pers.hll.aigc4chat.server.handler.MessageHandler;
import pers.hll.aigc4chat.server.handler.MessageHandlerName;
import pers.hll.aigc4chat.server.mapper.WechatMessageHandlerConfigMapper;
import pers.hll.aigc4chat.server.service.IWeChatMessageHandlerConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hll
 * @since 2024-04-22
 */
@Service
@RequiredArgsConstructor
public class WeChatMessageHandlerConfigServiceImpl
        extends ServiceImpl<WechatMessageHandlerConfigMapper, WeChatMessageHandlerConfig>
        implements IWeChatMessageHandlerConfigService {

    private final ApplicationContext applicationContext;

    @Override
    public String getHandlerName(String remarkName) {
        return lambdaQuery()
                .eq(WeChatMessageHandlerConfig::getRemarkName, remarkName)
                .oneOpt()
                .map(WeChatMessageHandlerConfig::getHandlerName)
                .orElse(null);
    }

    @Override
    public List<String> listHandlerName() {
        return ConstantUtil.listStringConstant(MessageHandlerName.class);
    }

    @Override
    public MessageHandler getMessageHandler(String beanName) {
        if (StringUtils.isEmpty(beanName)) {
            return (MessageHandler) applicationContext.getBean(MessageHandlerName.DEFAULT_MESSAGE_HANDLER);
        }
        return (MessageHandler) applicationContext.getBean(beanName);
    }
}
