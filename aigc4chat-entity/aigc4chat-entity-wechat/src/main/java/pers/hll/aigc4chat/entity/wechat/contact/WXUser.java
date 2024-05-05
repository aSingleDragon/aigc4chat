package pers.hll.aigc4chat.entity.wechat.contact;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 用户
 *
 * @author hll
 * @since 2024/03/10
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class WXUser extends WXContact {

    public static final int GENDER_UNKNOWN = 0;

    public static final int GENDER_MALE = 1;

    public static final int GENDER_FEMALE = 2;

    public static final int VERIFY_BIZ = 1;

    public static final int VERIFY_FAMOUS = 2;

    public static final int VERIFY_BIZ_BIG = 4;

    public static final int VERIFY_BIZ_BRAND = 8;

    public static final int VERIFY_BIZ_VERIFIED = 16;

    /**
     * 用户性别
     */
    private int gender;

    /**
     * 用户的个性签名
     */
    private String signature;

    /**
     * 自己给用户设置的备注名
     */
    private String remark;

    /**
     * 备注名的拼音首字母
     */
    private String remarkPY;

    /**
     * 备注名的拼音全拼
     */
    private String remarkQP;

    /**
     * 用户所在省份
     */
    private String province;

    /**
     * 用户所在城市
     */
    private String city;

    /**
     * 认证标志字段
     */
    private int verifyFlag;
}
