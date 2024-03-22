package pers.hll.aigc4chat.server.base;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 业务代码枚举
 *
 * @author hll
 * @since 2024/03/21
 */
@Getter
@AllArgsConstructor
public enum ResultCode implements IResultCode {

    SUCCESS(200, "操作成功"),

    FAILURE(400, "业务异常"),

    UN_AUTHORIZED(401, "请求未授权"),

    NOT_FOUND(404, "404 没找到请求"),

    MSG_NOT_READABLE(400, "消息不能读取"),

    METHOD_NOT_SUPPORTED(405, "不支持当前请求方法"),

    MEDIA_TYPE_NOT_SUPPORTED(415, "不支持当前媒体类型"),

    REQ_REJECT(403, "请求被拒绝"),

    INTERNAL_SERVER_ERROR(500, "服务器异常"),

    PARAM_MISS(400, "缺少必要的请求参数"),

    PARAM_TYPE_ERROR(400, "请求参数类型错误"),

    PARAM_BIND_ERROR(400, "请求参数绑定错误"),

    PARAM_VALID_ERROR(400, "参数校验失败");

    private final int code;

    private final String message;
}
