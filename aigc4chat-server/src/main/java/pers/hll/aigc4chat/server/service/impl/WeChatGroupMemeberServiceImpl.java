package pers.hll.aigc4chat.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import pers.hll.aigc4chat.server.bean.WeChatUserPageQuery;
import pers.hll.aigc4chat.server.entity.BaseEntity;
import pers.hll.aigc4chat.server.entity.WeChatGroupMember;
import pers.hll.aigc4chat.server.entity.WeChatUser;
import pers.hll.aigc4chat.server.mapper.WeChatGroupMemberMapper;
import pers.hll.aigc4chat.server.service.IWeChatGroupMemberService;
import pers.hll.aigc4chat.server.util.PageUtil;

import java.util.List;

/**
 * <p>
 * 群成员 服务实现类
 * </p>
 *
 * @author hll
 * @since 2024-04-14
 */
@Service
@RequiredArgsConstructor
public class WeChatGroupMemeberServiceImpl extends ServiceImpl<WeChatGroupMemberMapper, WeChatGroupMember>
        implements IWeChatGroupMemberService {

    private final WeChatGroupMemberMapper weChatGroupMemberMapper;

    @Override
    public List<WeChatGroupMember> listByGroupUserName(String groupUserName) {
        return list(new LambdaQueryWrapper<WeChatGroupMember>().eq(WeChatGroupMember::getGroupUserName, groupUserName));
    }

    @Override
    public IPage<WeChatUser> pageGroupMember(WeChatUserPageQuery query) {
        return weChatGroupMemberMapper.pageGroupMember(query, PageUtil.createPage(query));
    }

    @Override
    public List<WeChatUser> listGroupMember(String groupUserName) {
        return weChatGroupMemberMapper.listGroupMember(groupUserName);
    }
}
