package pers.hll.aigc4chat.server.wechat;

import pers.hll.aigc4chat.common.entity.wechat.contact.WXContact;
import pers.hll.aigc4chat.common.entity.wechat.contact.WXGroup;
import pers.hll.aigc4chat.common.entity.wechat.contact.WXUser;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.response.RspInit;

import java.util.HashMap;

/**
 * 模拟网页微信客户端联系人
 */
@SuppressWarnings("unchecked")
final class WeChatContacts {
    private final HashMap<String, WXContact> contacts = new HashMap<>();
    private final HashMap<String, WXUser> friends = new HashMap<>();
    private final HashMap<String, WXGroup> groups = new HashMap<>();
    private WXUser me;

    private static <T extends WXContact> T parseContact(String host, RspInit.User contact) {
        if (contact.UserName.startsWith("@@")) {
            WXGroup group = new WXGroup();
            group.setId(contact.UserName);
            group.setName(contact.NickName);
            group.setNamePY(contact.PYInitial);
            group.setNameQP(contact.PYQuanPin);
            group.setAvatarUrl(String.format("https://%s%s", host, contact.HeadImgUrl));
            group.setContactFlag(contact.ContactFlag);
            group.setDetail(false);
            group.setOwner(contact.IsOwner > 0);
            group.setMembers(new HashMap<>());
            for (RspInit.User user : contact.MemberList) {
                WXGroup.Member member = new WXGroup.Member();
                member.setId(user.UserName);
                member.setName(user.NickName);
                member.setDisplay(user.DisplayName);
                group.getMembers().put(member.getId(), member);
            }
            return (T) group;
        } else {
            WXUser user = new WXUser();
            user.setId(contact.UserName);
            user.setName(contact.NickName);
            user.setNamePY(contact.PYInitial);
            user.setNameQP(contact.PYQuanPin);
            user.setAvatarUrl(String.format("https://%s%s", host, contact.HeadImgUrl));
            user.setContactFlag(contact.ContactFlag);
            user.setGender(contact.Sex);
            user.setSignature(contact.Signature);
            user.setRemark(contact.RemarkName);
            user.setRemarkPY(contact.RemarkPYInitial);
            user.setRemarkQP(contact.RemarkPYQuanPin);
            user.setProvince(contact.Province);
            user.setCity(contact.City);
            user.setVerifyFlag(contact.VerifyFlag);
            return (T) user;
        }
    }

    /**
     * 获取自身信息
     *
     * @return 自身信息
     */
    WXUser getMe() {
        return this.me;
    }

    /**
     * 获取好友信息
     *
     * @param id 好友id
     * @return 好友信息
     */
    WXUser getFriend(String id) {
        return this.friends.get(id);
    }

    /**
     * 获取所有好友
     *
     * @return 所有好友
     */
    HashMap<String, WXUser> getFriends() {
        return this.friends;
    }

    /**
     * 获取群信息
     *
     * @param id 群id
     * @return 群信息
     */
    WXGroup getGroup(String id) {
        return this.groups.get(id);
    }

    /**
     * 获取所有群
     *
     * @return 所有群
     */
    HashMap<String, WXGroup> getGroups() {
        return this.groups;
    }

    /**
     * 获取联系人信息
     *
     * @param userId 联系人id
     * @return 联系人信息
     */
    WXContact getContact(String userId) {
        return this.contacts.get(userId);
    }

    /**
     * 设置自身信息
     *
     * @param userMe 自身信息
     */
    void setMe(String host, RspInit.User userMe) {
        this.me = WeChatContacts.parseContact(host, userMe);
        this.contacts.put(this.me.getId(), this.me);
    }

    void putContact(String host, RspInit.User userContact) {
        WXContact contact = WeChatContacts.parseContact(host, userContact);
        this.contacts.put(contact.getId(), contact);
        if (contact instanceof WXGroup wxGroup) {
            groups.put(wxGroup.getId(), wxGroup);
        } else {
            WXUser user = (WXUser) contact;
            if ((user.getContactFlag() & WXContact.CONTACT) > 0) {
                friends.put(user.getId(), user);
            }
        }
    }

    /**
     * 移除联系人
     *
     * @param userId 联系人id
     */
    WXContact rmvContact(String userId) {
        this.groups.remove(userId);
        this.friends.remove(userId);
        return this.contacts.remove(userId);
    }
}