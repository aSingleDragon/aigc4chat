package pers.hll.aigc4chat.model.ollama.constant;

/**
 * Ollama end point
 *
 * @author hll
 * @since 2024/04/30
 */
public final class EndPoint {

    private EndPoint() {
    }

    private static final String API = "/api";

    /**
     * 使用提供的模型为给定的提示生成响应。
     * 这是一个流式传输的端点，因此将有一系列的响应。
     * 最终的响应对象将包括来自请求的统计信息和其他数据。
     */
    public static final String GENERATE = API + "/generate";

    /**
     * 使用提供的模型在聊天中生成下一条消息。
     * 这是一个流式传输的端点，因此将有一系列的响应。
     * 可以通过将“stream”设置为false来禁用流式传输。
     * 最终的响应对象将包含请求的统计信息和其他数据。
     */
    public static final String CHAT = API + "/chat";


    /**
     * 从 Modelfile 创建一个模型。
     * 建议将 modelfile 设置为 Modelfile 的内容，而不仅仅是设置路径。这是远程创建的要求。
     * 远程模型创建还必须使用“创建Blob”和响应中指示的路径的值，在服务器上显式地创建任何文件块，如FROM和ADAPTER等字段。
     */
    public static final String CREATE = API + "/create";

    /**
     * 列出本地可用的模型。
     */
    public static final String TAGS = API + "/tags";

    /**
     * 显示有关模型的信息，包括详细信息、Modelfile、模板、参数、许可证和系统提示。
     */
    public static final String SHOW = API + "/show";

    /**
     * 复制模型。从现有模型创建一个具有另一个名称的模型。
     */
    public static final String COPY = API + "/copy";

    /**
     * 删除一个模型及其数据
     */
    public static final String DELETE = API + "/delete";

    /**
     * 从ollama库中下载模型。取消的拉取操作将从上次中断的地方继续，多次调用将共享相同的下载进度。
     */
    public static final String PULL = API + "/pull";

    /**
     * 将模型上传到模型库。需要先注册 ollama.ai 并添加一个公钥。
     */
    public static final String PUSH = API + "/push";

    /**
     * 从模型中生成嵌入（Embeddings）
     */
    public static final String EMBEDDINGS = API + "/embeddings";
}
