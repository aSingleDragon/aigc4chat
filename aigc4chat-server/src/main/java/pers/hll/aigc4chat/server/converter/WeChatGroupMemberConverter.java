package pers.hll.aigc4chat.server.converter;
import pers.hll.aigc4chat.protocol.wechat.response.webwxinit.User;
import pers.hll.aigc4chat.server.entity.WeChatGroupMember;

import lombok.experimental.UtilityClass;
import java.util.Collections;
import java.util.List;

/**
* 实体类转换Converter
* <p> 代码由 ConverterUtil 生成
*
* @author hll
* @since 2024-04-06
*/
@UtilityClass
public class WeChatGroupMemberConverter {

    public static WeChatGroupMember from(User user, String groupUserName) {
        if (user == null) {
            return null;
        }
        WeChatGroupMember weChatGroupMember = new WeChatGroupMember();
        weChatGroupMember.setUserName(user.getUserName());
        weChatGroupMember.setNickName(user.getNickName());
        weChatGroupMember.setDisplayName(user.getDisplayName());
        weChatGroupMember.setGroupUserName(groupUserName);
        return weChatGroupMember;
    }

    public static List<WeChatGroupMember> from(List<User> userList, String groupUserName) {
        if (userList == null) {
            return Collections.emptyList();
        }
        return userList
                .stream()
                .map(x -> from(x, groupUserName))
                .toList();
    }
}