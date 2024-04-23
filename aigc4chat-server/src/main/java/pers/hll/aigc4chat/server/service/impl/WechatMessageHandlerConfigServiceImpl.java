package pers.hll.aigc4chat.server.service.impl;

import pers.hll.aigc4chat.common.base.util.ConstantUtil;
import pers.hll.aigc4chat.server.entity.WechatMessageHandlerConfig;
import pers.hll.aigc4chat.server.handler.MessageHandlerName;
import pers.hll.aigc4chat.server.mapper.WechatMessageHandlerConfigMapper;
import pers.hll.aigc4chat.server.service.IWechatMessageHandlerConfigService;
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
public class WechatMessageHandlerConfigServiceImpl
        extends ServiceImpl<WechatMessageHandlerConfigMapper, WechatMessageHandlerConfig>
        implements IWechatMessageHandlerConfigService {

    @Override
    public String getHandlerName(String userName) {
        return lambdaQuery()
                .eq(WechatMessageHandlerConfig::getUserName, userName)
                .oneOpt()
                .map(WechatMessageHandlerConfig::getHandlerName)
                .orElse(null);
    }

    @Override
    public List<String> listHandlerName() {
        return ConstantUtil.listStringConstant(MessageHandlerName.class);
    }
}
