package pers.hll.aigc4chat.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import pers.hll.aigc4chat.server.entity.User;
import pers.hll.aigc4chat.server.mapper.UserMapper;
import pers.hll.aigc4chat.server.service.IUserService;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author Leo825
 * @since 2022-07-05
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
