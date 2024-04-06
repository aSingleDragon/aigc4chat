package pers.hll.aigc4chat.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.WeChatHttpClient;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.request.WebWxBatchGetContactReq;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.request.WebWxGetContactReq;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.request.body.Contact;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.response.WebWxBatchGetContactResp;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.response.WebWxGetContactResp;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.response.webwxinit.User;
import pers.hll.aigc4chat.server.converter.WeChatGroupMemberConverter;
import pers.hll.aigc4chat.server.converter.WeChatUserConverter;
import pers.hll.aigc4chat.server.entity.WeChatGroupMember;
import pers.hll.aigc4chat.server.entity.WeChatUser;
import pers.hll.aigc4chat.server.mapper.WeChatUserMapper;
import pers.hll.aigc4chat.server.service.IWeChatGroupMemberService;
import pers.hll.aigc4chat.server.service.IWeChatUserService;
import pers.hll.aigc4chat.server.wechat.WeChatRequestCache;
import pers.hll.aigc4chat.server.wechat.WeChatTool;

import java.util.List;

import static pers.hll.aigc4chat.common.protocol.wechat.protocol.constant.WXEndPoint.WEB_WX_BATCH_GET_CONTACT;
import static pers.hll.aigc4chat.common.protocol.wechat.protocol.constant.WXEndPoint.WEB_WX_GET_GET_CONTACT;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author Leo825
 * @since 2022-07-05
 */
@Service
@RequiredArgsConstructor
public class WeChatUserServiceImpl extends ServiceImpl<WeChatUserMapper, WeChatUser> implements IWeChatUserService {


    @Override
    public void saveOrUpdateMe(WeChatUser me) {
        // 每次获取的用户的userName都会变 登录的用户的uin不会变 删除旧的用户
        list(new LambdaQueryWrapper<WeChatUser>().eq(WeChatUser::getUin, me.getUin())).forEach(this::removeById);
        save(me);
    }

    @Override
    public WeChatUser selectMe() {
        return getOne(new LambdaQueryWrapper<WeChatUser>().gt(WeChatUser::getUin, 0));
    }
}
