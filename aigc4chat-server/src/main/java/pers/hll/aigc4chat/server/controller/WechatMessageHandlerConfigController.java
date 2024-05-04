package pers.hll.aigc4chat.server.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import pers.hll.aigc4chat.base.exception.BizException;
import pers.hll.aigc4chat.base.util.EasyCollUtil;
import pers.hll.aigc4chat.server.base.R;
import pers.hll.aigc4chat.server.entity.WeChatMessageHandlerConfig;
import pers.hll.aigc4chat.server.entity.WeChatUser;
import pers.hll.aigc4chat.server.service.IWeChatUserService;
import pers.hll.aigc4chat.server.service.IWeChatMessageHandlerConfigService;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author hll
 * @since 2024-04-22
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/message-handler")
@Tag(name = "WechatMessageHandlerConfigController", description = "消息处理器控制器")
public class WechatMessageHandlerConfigController {

    private final IWeChatMessageHandlerConfigService weChatMessageHandlerConfigService;

    private final IWeChatUserService weChatUserService;

    @GetMapping("/list/handler-name")
    @Operation(summary = "处理器名称列表", description = "获得所有处理器的名称")
    public R<List<String>> listHandlerName() {
        return R.data(weChatMessageHandlerConfigService.listHandlerName());
    }

    @PostMapping("/save-or-update")
    @Operation(summary = "新增/更新消息处理器配置", description = "增加或更新某用户对应的消息处理器")
    public R<String> saveOrUpdate(@RequestParam @Parameter(description = "用户名(请保证用户的备注非空且唯一)") String userName,
                                  @RequestParam @Parameter(description = "处理器名称") String handlerName) {
        if (!weChatMessageHandlerConfigService.listHandlerName().contains(handlerName)) {
            throw new IllegalArgumentException("未找到[" + handlerName + "]对应的消息处理器!");
        }
        List<WeChatUser> weChatUserList = weChatUserService.listByName(userName);
        List<String> sameRemarkNameList = EasyCollUtil.getFieldList(weChatUserList, WeChatUser::getRemarkName);
        if (!CollectionUtils.isEmpty(sameRemarkNameList)) {
            throw BizException.of("用户[{}]的备注为空!", userName);
        }
        if (sameRemarkNameList.size() > 1) {
            throw BizException.of("用户[{}]的备注不唯一[有{}个]!", userName, sameRemarkNameList.size());
        }
        weChatMessageHandlerConfigService.saveOrUpdate(
                new WeChatMessageHandlerConfig(sameRemarkNameList.get(0), handlerName));
        return R.success();
    }

    @GetMapping("/get-handler-name")
    @Operation(summary = "获取用户对应的处理器名称", description = "获取用户对应的处理器名称")
    public R<?> getHandlerName(@RequestParam @Parameter(description = "用户名称") String name) {
        return R.data(weChatMessageHandlerConfigService.getHandlerName(weChatUserService.getOneByName(name)));
    }
}
