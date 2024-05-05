package pers.hll.aigc4chat.server.handler;

/**
 * 消息处理器名称
 *
 * @author hll
 * @since 2024/04/21
 */
public final class MessageHandlerName {

    private MessageHandlerName() {}

    public static final String DEFAULT_MESSAGE_HANDLER = "doNothing";

    public static final String AUDIO_REPEATER_MESSAGE_HANDLER = "audioRepeater";

    public static final String OLLAMA_MESSAGE_HANDLER = "ollama";
}
