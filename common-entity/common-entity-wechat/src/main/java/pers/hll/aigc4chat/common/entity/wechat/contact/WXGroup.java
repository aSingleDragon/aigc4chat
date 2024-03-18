package pers.hll.aigc4chat.common.entity.wechat.contact;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.HashMap;

/**
 * 微信群
 *
 * @author hll
 * @since 2023/03/10
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WXGroup extends WXContact implements Serializable, Cloneable {

    /**
     * 是否是详细的群信息（主要是是否获取过群成员）。
     * 如果不是则可以通过WeChatClient.fetchContact方法获取群的详细信息。
     */
    private boolean isDetail;

    /**
     * 我自己是否是群主
     */
    private boolean isOwner;

    /**
     * 群成员id到entity的映射
     */
    private HashMap<String, Member> members;

    @Override
    public WXGroup clone() {
        WXGroup wxGroup = (WXGroup) super.clone();
        if (this.members != null) {
            wxGroup.members = (HashMap<String, Member>) this.members.clone();
        }
        return wxGroup;
    }
}
