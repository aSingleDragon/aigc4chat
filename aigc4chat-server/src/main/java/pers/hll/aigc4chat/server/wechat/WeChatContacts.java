package pers.hll.aigc4chat.server.wechat;

import lombok.Getter;
import pers.hll.aigc4chat.common.entity.wechat.contact.Member;
import pers.hll.aigc4chat.common.entity.wechat.contact.WXContact;
import pers.hll.aigc4chat.common.entity.wechat.contact.WXGroup;
import pers.hll.aigc4chat.common.entity.wechat.contact.WXUser;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.response.webwxinit.User;

import java.util.HashMap;

/**
 * 模拟网页微信客户端联系人
 *
 * @author hll
 * @since 2024/03/19
 */
public final class WeChatContacts {

    private final HashMap<String, WXContact> contacts = new HashMap<>();

    @Getter
    private final HashMap<String, WXUser> friends = new HashMap<>();

    @Getter
    private final HashMap<String, WXGroup> groups = new HashMap<>();

    @Getter
    private WXUser me;

    private static <T extends WXContact> T parseContact(String host, User contact) {
        if (contact.getUserName().startsWith("@@")) {
            WXGroup group = new WXGroup();
            group.setId(contact.getUserName());
            group.setName(contact.getNickName());
            group.setNamePY(contact.getPyInitial());
            group.setNameQP(contact.getPyQuanPin());
            group.setAvatarUrl(String.format("https://%s%s", host, contact.getHeadImgUrl()));
            group.setContactFlag(contact.getContactFlag());
            group.setDetail(false);
            group.setOwner(contact.getIsOwner() > 0);
            group.setMembers(new HashMap<>());
            for (User user : contact.getMemberList()) {
                Member member = new Member();
                member.setId(user.getUserName());
                member.setName(user.getNickName());
                member.setDisplay(user.getDisplayName());
                group.getMembers().put(member.getId(), member);
            }
            return (T) group;
        } else {
            WXUser user = new WXUser();
            user.setId(contact.getUserName());
            user.setName(contact.getNickName());
            user.setNamePY(contact.getPyInitial());
            user.setNameQP(contact.getPyQuanPin());
            user.setAvatarUrl(String.format("https://%s%s", host, contact.getHeadImgUrl()));
            user.setContactFlag(contact.getContactFlag());
            user.setGender(contact.getSex());
            user.setSignature(contact.getSignature());
            user.setRemark(contact.getRemarkName());
            user.setRemarkPY(contact.getRemarkPyInitial());
            user.setRemarkQP(contact.getRemarkPyQuanPin());
            user.setProvince(contact.getProvince());
            user.setCity(contact.getCity());
            user.setVerifyFlag(contact.getVerifyFlag());
            return (T) user;
        }
    }

    /**
     * 获取好友信息
     *
     * @param id 好友id
     * @return 好友信息
     */
    public WXUser getFriend(String id) {
        return this.friends.get(id);
    }

    /**
     * 获取群信息
     *
     * @param id 群id
     * @return 群信息
     */
    public WXGroup getGroup(String id) {
        return this.groups.get(id);
    }

    /**
     * 获取联系人信息
     *
     * @param userId 联系人id
     * @return 联系人信息
     */
    public WXContact getContact(String userId) {
        return this.contacts.get(userId);
    }

    /**
     * 设置自身信息
     *
     * @param userMe 自身信息
     */
    public void setMe(String host, User userMe) {
        this.me = WeChatContacts.parseContact(host, userMe);
        this.contacts.put(this.me.getId(), this.me);
    }

    public void putContact(String host, User userContact) {
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
    public WXContact rmvContact(String userId) {
        this.groups.remove(userId);
        this.friends.remove(userId);
        return this.contacts.remove(userId);
    }
}
