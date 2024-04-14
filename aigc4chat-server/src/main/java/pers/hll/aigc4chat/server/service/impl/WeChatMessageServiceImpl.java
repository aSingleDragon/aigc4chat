package pers.hll.aigc4chat.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import pers.hll.aigc4chat.server.bean.WeChatMessagePageQuery;
import pers.hll.aigc4chat.server.bean.WeChatUserPageQuery;
import pers.hll.aigc4chat.server.entity.BaseEntity;
import pers.hll.aigc4chat.server.entity.WeChatMessage;
import pers.hll.aigc4chat.server.entity.WeChatUser;
import pers.hll.aigc4chat.server.mapper.WeChatMessageMapper;
import pers.hll.aigc4chat.server.service.IWeChatMessageService;
import pers.hll.aigc4chat.server.util.PageUtil;

/**
 * <p>
 * 消息 服务实现类
 * </p>
 *
 * @author hll
 * @since 2024-04-14
 */
@Service
@RequiredArgsConstructor
public class WeChatMessageServiceImpl extends ServiceImpl<WeChatMessageMapper, WeChatMessage>
        implements IWeChatMessageService {

    private final WeChatMessageMapper weChatMessageMapper;

    @Override
    public IPage<WeChatMessage> pageMessage(WeChatMessagePageQuery query) {
        IPage<WeChatMessage> page = PageUtil.createPage(query);
        return weChatMessageMapper.selectPage(page, new LambdaQueryWrapper<WeChatMessage>()
                .eq(StringUtils.isNotEmpty(query.getFormUserName()), WeChatMessage::getFromUserName, query.getFormUserName())
                .eq(StringUtils.isNotEmpty(query.getToUserName()), WeChatMessage::getToUserName, query.getToUserName())
                .orderByAsc(BaseEntity::getUpdatedTime));
    }
}
