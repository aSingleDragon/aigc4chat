package pers.hll.aigc4chat.common.entity.wechat.contact;

import lombok.Data;

import java.io.Serializable;

/**
 *
 * @author hll
 * @since 2024/03/19
 */
@Data
public class Member implements Serializable, Cloneable {

    /**
     * 群成员id
     */
    private String id;

    /**
     * 群成员昵称
     */
    private String name;

    /**
     * 群成员群名片
     */
    private String display;

    @Override
    public Member clone() {
        try {
            return (Member) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException();
        }
    }
}