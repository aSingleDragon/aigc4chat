package pers.lys.aigc4chat.model.baidu.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 对话回复模型枚举
 *
 * @author LiYaosheng
 * @since 2024/3/26
 */
@Getter
@RequiredArgsConstructor
public enum ReplyModeEnum {

    REPEATER("Repeater","复读机模式"),

    AGENT("Agent","Agent自定义对话模型"),

    ERNIE("Ernie","文心一言 ERNIE-Lite-8K-0922模型");

    /**
     * 编码
     */
    private final String code;

    /**
     * 描述
     */
    private final String desc;
}
