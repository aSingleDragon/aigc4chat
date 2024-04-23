package pers.hll.aigc4chat.server.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pers.hll.aigc4chat.server.base.R;
import pers.hll.aigc4chat.server.base.ResultCode;

/**
 * 全局异常处理
 */
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

    @ExceptionHandler(Exception.class)
    public R<String> handleException(Exception ex) {
        return R.fail(ex.getMessage());
    }
}