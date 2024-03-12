package pers.hll.aigc4chat.server.wechat;


import pers.hll.aigc4chat.common.base.XTools;
import pers.hll.aigc4chat.common.base.http.executor.impl.XHttpExecutorImpl;

import javax.annotation.Nonnull;

/**
 * 微信HTTP请求执行器，增加了User-Agent请求头，支持配置文件配置
 * <ul>
 * <li>[2019-05-30 09:31]XXX：初始创建</li>
 * </ul>
 *
 * @author hll
 * @since 2024/03/10
 */
public class WXHttpExecutor extends XHttpExecutorImpl {

    public static final String CFG_USERAGENT = WeChatClient.CFG_PREFIX + "userAgent";

    public static final String CFG_USERAGENT_DEFAULT =
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.71 Safari/537.36";

    private final String userAgent = XTools.cfgDef(CFG_USERAGENT, CFG_USERAGENT_DEFAULT);

    @Nonnull
    @Override
    public Response execute(@Nonnull Request request) throws Exception {
        request.setHeader("User-Agent", userAgent, false);
        return super.execute(request);
    }
}
