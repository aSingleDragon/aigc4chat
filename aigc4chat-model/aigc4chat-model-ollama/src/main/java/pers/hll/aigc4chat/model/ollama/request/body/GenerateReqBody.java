package pers.hll.aigc4chat.model.ollama.request.body;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 调用 /api/generate 接口的请求体
 *
 * @author hll
 * @since 2024/04/29
 */
@Data
@Builder
public class GenerateReqBody<T> {

    /**
     * 必填项，模型名称
     */
    private String model;

    /**
     * 必填项，要生成响应的提示文本 如果提供了一个空的prompt，模型将被加载到内存中。
     */
    private String prompt;

    /**
     * 可选项，Base64编码的图片列表（适用于多模态模型如llava）
     */
    private List<String> images;

    /**
     * 可选项，返回响应的格式，默认为"json"。目前仅接受"json"作为有效值。
     */
    private String format;

    /**
     * 可选项，额外的模型参数对象，其具体内容可在模型文件的文档中查看
     * 如温度（temperature）等参数
     */
    private T options;

    /**
     * 可选项，系统消息，将覆盖在 Model file 中定义的消息
     */
    private String system;

    /**
     * 可选项，使用的提示模板，将覆盖 Model file 中定义的模板
     */
    private String template;

    /**
     * 可选项，上一次请求生成API时返回的上下文参数，
     * 可用于保持简短的对话记忆
     */
    private List<Integer> context;

    /**
     * 可选项，若设为false，响应将以单个响应对象而非流的形式返回
     * 默认false
     */
    private Boolean stream;

    /**
     * 可选项，若设为true，则不对提示进行格式化。
     * 若您在请求中提供了完整的模板提示，则可以选择使用此参数
     */
    private Boolean raw;

    /**
     * 可选项，控制模型在请求结束后在内存中保留的时间，默认为5分钟（如"5m"）
     */
    @SerializedName("keep_alive")
    private String keepAlive;
}