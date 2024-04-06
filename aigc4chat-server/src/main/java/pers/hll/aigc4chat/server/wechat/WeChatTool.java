package pers.hll.aigc4chat.server.wechat;

import lombok.experimental.UtilityClass;

/**
 * 微信工具类
 *
 * @author hll
 * @since 2024/04/06
 */
@UtilityClass
public class WeChatTool {

    public boolean isFriend(String userName) {
        return userName.startsWith("@") && !userName.startsWith("@@");
    }

    public boolean isGroup(String userName) {
        return userName.startsWith("@@");
    }

    public boolean isNotGroup(String userName) {
        return !isGroup(userName);
    }
}
