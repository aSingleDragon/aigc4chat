package pers.hll.aigc4chat.server.base;

import java.io.Serializable;

/**
 * 响应码接口
 *
 * @author hll
 * @since 2024/03/21
 */
public interface IResultCode extends Serializable {

    /**
     * 消息
     * @return String
     */
    String getMessage();

    /**
     * 状态码
     * @return int
     */
    int getCode();
}
