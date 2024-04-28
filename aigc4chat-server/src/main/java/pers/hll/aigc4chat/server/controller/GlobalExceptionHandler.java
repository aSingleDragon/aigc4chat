package pers.hll.aigc4chat.server.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pers.hll.aigc4chat.server.base.R;
import pers.hll.aigc4chat.server.base.ResultCode;
import pers.hll.aigc4chat.base.exception.BizException;

/**
 * 全局异常处理
 *
 * @author hll
 * @since 2024/04/25
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public R<String> handleIllegalArgument(IllegalArgumentException ex) {
        return R.fail("非法参数异常: " + ex.getMessage());
    }

    @ExceptionHandler(NullPointerException.class)
    public R<String> handleNullPointerException(NullPointerException ex) {
        return R.fail(ResultCode.INTERNAL_SERVER_ERROR,"空指针异常：" + ex.getMessage());
    }

    @ExceptionHandler(BizException.class)
    public R<String> handleException(BizException ex) {
        log.error("业务异常：{}", ex.getMessage(), ex);
        return R.fail(ex.getMessage());
    }
}