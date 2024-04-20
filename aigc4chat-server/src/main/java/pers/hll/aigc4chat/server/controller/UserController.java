package pers.hll.aigc4chat.server.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pers.hll.aigc4chat.server.base.R;
import pers.hll.aigc4chat.server.bean.WeChatUserPageQuery;
import pers.hll.aigc4chat.server.entity.WeChatUser;
import pers.hll.aigc4chat.server.service.IWeChatGroupMemberService;
import pers.hll.aigc4chat.server.service.IWeChatUserService;

import java.util.List;

/**
 * 用户控制器 (支持离线)
 *
 * @author hll
 * @since 2024/04/14
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Tag(name = "UserController", description = "用户控制器")
public class UserController {

    private final IWeChatUserService weChatUserService;

    private final IWeChatGroupMemberService weChatGroupMemberService;

    @GetMapping("/page/group")
    public R<IPage<WeChatUser>> pageGroup(WeChatUserPageQuery query) {
        return R.data(weChatUserService.pageGroup(query));
    }

    @GetMapping("/page/group/member")
    public R<IPage<WeChatUser>> pageGroupMember(WeChatUserPageQuery query) {
        return R.data(weChatGroupMemberService.pageGroupMember(query));
    }

    @GetMapping("/list/group/member")
    public R<List<WeChatUser>> listGroupMember(String groupUserName) {
        return R.data(weChatGroupMemberService.listGroupMember(groupUserName));
    }

    @GetMapping("/page/friend")
    public R<IPage<WeChatUser>> pageFriend(WeChatUserPageQuery query) {
        return R.data(weChatUserService.pageFriend(query));
    }

    @GetMapping("/query")
    public R<List<WeChatUser>> query(String name) {
        return R.data(weChatUserService.listByName(name));
    }
}
