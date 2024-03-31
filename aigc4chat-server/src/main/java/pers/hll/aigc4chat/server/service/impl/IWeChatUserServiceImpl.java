package pers.hll.aigc4chat.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import pers.hll.aigc4chat.server.entity.WeChatUser;
import pers.hll.aigc4chat.server.mapper.WeChatUserMapper;
import pers.hll.aigc4chat.server.service.IWeChatUserService;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author Leo825
 * @since 2022-07-05
 */
@Service
public class IWeChatUserServiceImpl extends ServiceImpl<WeChatUserMapper, WeChatUser> implements IWeChatUserService {

}
