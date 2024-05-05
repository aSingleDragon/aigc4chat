package pers.hll.aigc4chat.model.gemini;

import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.generativeai.*;

import java.io.IOException;
import java.util.Base64;


/**
 * <a href="https://apifox.com/apiskills/how-to-use-gemini-api/">如何使用Gemini API</a>
 * Gemini 测试 随便写写 不要在意
 *
 * @author hll
 * @since 2024/04/26
 */
public class GeminiExample {

    public static void main(String[] args) throws IOException {
        String projectId = "your-project-id";
        String location = "us-central1";
        String modelName = "gemini-pro-vision";
        try (VertexAI ai = new VertexAI(projectId, location)) {
            byte[] imageBytes = Base64.getDecoder().decode("");

            GenerativeModel model = new GenerativeModel(modelName, ai);
            GenerateContentResponse response = model.generateContent(
                    ContentMaker.fromMultiModalData(
                            "这张图片关于什么",
                            PartMaker.fromMimeTypeAndData("image/jpg", imageBytes)));
            System.out.println(ResponseHandler.getText(response));

            model.generateContentStream("为什么现在这么卷?")
                    .stream()
                    .forEach(System.out::print);

            ChatSession chatSession = new ChatSession(model);

            response = chatSession.sendMessage("你好.");
            System.out.println(ResponseHandler.getText(response));

            response = chatSession.sendMessage("减脂餐吃什么好?");
            System.out.println(ResponseHandler.getText(response));

            response = chatSession.sendMessage("帮我制定一个减脂餐列表?");
            System.out.println(ResponseHandler.getText(response));
        }
    }
}
