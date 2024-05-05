package pers.hll.aigc4chat.entity.wechat.contact;

import lombok.Data;

/**
 * 群成员
 *
 * @author hll
 * @since 2024/03/19
 */
@Data
public class Member {

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
}