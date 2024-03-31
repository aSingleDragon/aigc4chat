package pers.hll.aigc4chat.server.converter;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.response.webwxinit.User;
import pers.hll.aigc4chat.server.entity.WeChatUser;

import lombok.experimental.UtilityClass;
import java.util.Collections;
import java.util.List;

/**
* 实体类转换Converter
* <p> 代码由 ConverterUtil 生成
*
* @author hll
* @since 2024-03-31
*/
@UtilityClass
public class WeChatUserConverter {

    public static WeChatUser from(User user) {
        if (user == null) {
            return null;
        }
        WeChatUser weChatUser = new WeChatUser();
        weChatUser.setProvince(user.getProvince());
        weChatUser.setPyQuanPin(user.getPyQuanPin());
        weChatUser.setAppAccountFlag(user.getAppAccountFlag());
        weChatUser.setContactFlag(user.getContactFlag());
        weChatUser.setEncryptChatRoomId(user.getEncryptChatRoomId());
        weChatUser.setUserName(user.getUserName());
        weChatUser.setStatues(user.getStatues());
        weChatUser.setPyInitial(user.getPyInitial());
        weChatUser.setHeadImgFlag(user.getHeadImgFlag());
        weChatUser.setMemberCount(user.getMemberCount());
        weChatUser.setMemberStatus(user.getMemberStatus());
        weChatUser.setUin(user.getUin());
        weChatUser.setDisplayName(user.getDisplayName());
        weChatUser.setVerifyFlag(user.getVerifyFlag());
        weChatUser.setAlias(user.getAlias());
        weChatUser.setSignature(user.getSignature());
        weChatUser.setRemarkPyQuanPin(user.getRemarkPyQuanPin());
        weChatUser.setNickName(user.getNickName());
        weChatUser.setIsOwner(user.getIsOwner());
        weChatUser.setWebWxPluginSwitch(user.getWebWxPluginSwitch());
        weChatUser.setHeadImgUrl(user.getHeadImgUrl());
        weChatUser.setUniFriend(user.getUniFriend());
        weChatUser.setAttrStatus(user.getAttrStatus());
        weChatUser.setChatRoomId(user.getChatRoomId());
        weChatUser.setStarFriend(user.getStarFriend());
        weChatUser.setRemarkName(user.getRemarkName());
        weChatUser.setRemarkPyInitial(user.getRemarkPyInitial());
        weChatUser.setCity(user.getCity());
        weChatUser.setSex(user.getSex());
        weChatUser.setKeyWord(user.getKeyWord());
        weChatUser.setSnsFlag(user.getSnsFlag());
        weChatUser.setOwnerUin(user.getOwnerUin());
        weChatUser.setHideInputBarFlag(user.getHideInputBarFlag());
        return weChatUser;
    }

    public static List<WeChatUser> from(List<User> userList) {
        if (userList == null) {
            return Collections.emptyList();
        }
        return userList
                .stream()
                .map(WeChatUserConverter::from)
                .toList();
    }
}