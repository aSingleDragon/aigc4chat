package pers.hll.aigc4chat.server.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * <p>
 *     微信消息处理器配置
 * </p>
 *
 * @author hll
 * @since 2024-04-22
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("wechat_message_handler_config")
@Schema(name = "微信消息处理器配置")
public class WeChatMessageHandlerConfig extends BaseEntity {

    @TableId
    private String remarkName;

    private String handlerName;
}
