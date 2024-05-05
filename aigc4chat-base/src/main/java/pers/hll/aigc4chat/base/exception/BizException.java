package pers.hll.aigc4chat.base.exception;

import org.slf4j.helpers.MessageFormatter;
import org.slf4j.helpers.NormalizedParameters;

/**
 * 业务异常
 *
 * @author hll
 * @since 2024/04/26
 */
public class BizException extends RuntimeException {

    private BizException(String message) {
        super(message);
    }

    private BizException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 根据异常信息生成BizException
     * <pre>{@code
     *     if (obj == null) {
     *         throw BizException.of("obj not be null!");
     *     }
     * }
     * </pre>
     *
     * @param message 异常描述信息
     * @return BizException 异常
     */
    public static BizException of(String message) {
        return new BizException(message);
    }

    /**
     * 根据异常信息，模式和参数生成BizException
     * <pre>{@code
     *     try {
     *         ...
     *     } catch (Exception ex) {
     *         throw BizException.of("There is a exception:", ex);
     *     }
     * }
     *
     * </pre>
     * @param message 异常信息描述
     * @param cause 异常
     * @return BizException 异常
     */
    public static BizException of(String message, Throwable cause) {
        return new BizException(message, cause);
    }

    /**
     * 根据异常信息，模式和参数生成BizException
     * <pre>{@code
     *     try {
     *         ...
     *         id = ...
     *         ...
     *     } catch (Exception ex) {
     *         throw BizException.of("{} is not found", id, ex);
     *     }
     * }
     * </pre>
     *
     * @param pattern   异常描述信息模式
     * @param arguments 异常描述信息模式参数列表 如果有{@link Throwable}的参数, 请放在参数列表最后一个位置
     * @return BizException 异常
     */
    public static BizException of(String pattern, Object... arguments) {
        Throwable throwableCandidate = NormalizedParameters.getThrowableCandidate(arguments);
        String formatMessage;
        if (throwableCandidate != null) {
            arguments = MessageFormatter.trimmedCopy(arguments);
        }
        formatMessage = MessageFormatter.arrayFormat(pattern, arguments).getMessage();
        return BizException.of(formatMessage, throwableCandidate);
    }
}
