package pers.hll.aigc4chat.model.ollama.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 角色
 *
 * @author hll
 * @see pers.hll.aigc4chat.model.ollama.request.body.Message
 * @since 2024/05/04
 */
@Getter
@RequiredArgsConstructor
public enum Role {

    USER("user"),
    SYSTEM("system"),
    ASSISTANT("assistant");

    private final String code;

    public static Role codeOf(String code) {
        for (Role role : Role.values()) {
            if (role.getCode().equals(code)) {
                return role;
            }
        }
        return USER;
    }
}
