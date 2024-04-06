package pers.hll.aigc4chat.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import pers.hll.aigc4chat.server.entity.WeChatGroupMember;
import pers.hll.aigc4chat.server.mapper.WeChatGroupMemberMapper;
import pers.hll.aigc4chat.server.service.IWeChatGroupMemberService;

import java.util.List;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author Leo825
 * @since 2022-07-05
 */
@Service
public class WeChatGroupMemeberServiceImpl extends ServiceImpl<WeChatGroupMemberMapper, WeChatGroupMember>
        implements IWeChatGroupMemberService {

    @Override
    public List<WeChatGroupMember> listByGroupUserName(String groupUserName) {
        return list(new LambdaQueryWrapper<WeChatGroupMember>().eq(WeChatGroupMember::getGroupUserName, groupUserName));
    }
}
