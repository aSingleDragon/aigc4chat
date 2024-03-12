package pers.hll.aigc4chat.common.entity.wechat.contact;

import lombok.Data;

import java.io.File;
import java.io.Serializable;

/**
 * 微信联系人
 *
 * @author hll
 * @since 2024/03/10
 */
@Data
public abstract class WXContact implements Serializable, Cloneable {

    public static final int CONTACT = 1;

    public static final int CONTACT_CHAT = 2;

    public static final int CONTACT_CHATROOM = 4;

    public static final int CONTACT_BLACKLIST = 8;

    public static final int CONTACT_DOMAIN = 16;

    public static final int CONTACT_HIDE = 32;

    public static final int CONTACT_FAVOUR = 64;

    public static final int CONTACT_3RDAPP = 128;

    public static final int CONTACT_SNSBLACKLIST = 256;

    public static final int CONTACT_NOTIFYCLOSE = 512;

    public static final int CONTACT_TOP = 2048;

    /**
     * 账户id，以@@开头的是群组，以@开头的是普通用户，其他的是特殊用户比如文件助手等
     */
    protected String id;

    /**
     * 账户的名称
     */
    protected String name;

    /**
     * 账户名称的拼音的首字母
     */
    protected String namePY;

    /**
     * 账户名称的拼音全拼
     */
    protected String nameQP;

    /**
     * 账户头像地址
     */
    protected String avatarUrl;

    /**
     * 账户头像文件
     */
    protected File avatarFile;

    /**
     * 联系人标志字段
     */
    protected int contactFlag;

    @Override
    public WXContact clone() {
        try {
            return (WXContact) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
