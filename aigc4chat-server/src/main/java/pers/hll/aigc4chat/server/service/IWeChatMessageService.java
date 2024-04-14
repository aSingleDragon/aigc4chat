package pers.hll.aigc4chat.server.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import pers.hll.aigc4chat.server.bean.WeChatMessagePageQuery;
import pers.hll.aigc4chat.server.entity.WeChatMessage;

/**
 * <p>
 * 消息 接口
 * </p>
 *
 * @author hll
 * @since 2024/03/31
 */
public interface IWeChatMessageService extends IService<WeChatMessage> {

    IPage<WeChatMessage> pageMessage(WeChatMessagePageQuery query);
}
