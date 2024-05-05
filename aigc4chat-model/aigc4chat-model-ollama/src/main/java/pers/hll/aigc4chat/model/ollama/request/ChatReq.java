package pers.hll.aigc4chat.model.ollama.request;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import pers.hll.aigc4chat.base.exception.BizException;
import pers.hll.aigc4chat.model.ollama.request.body.ChatReqBody;
import pers.hll.aigc4chat.model.ollama.response.body.ChatRespBody;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 *
 * @author hll
 * @since 2024/05/04
 */
@Slf4j
public class ChatReq extends BasePostRequest<ChatReqBody<?>, ChatRespBody> {

    public ChatReq(String uri) {
        super(uri);
    }

    @Override
    public ChatRespBody convertHttpEntityToObj(HttpEntity httpEntity) {
        if (getRequestBody().getStream() == null || getRequestBody().getStream()) {
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(httpEntity.getContent(), StandardCharsets.UTF_8))) {
                String line;
                StringBuilder content = new StringBuilder();
                ChatRespBody chatRespBody = new ChatRespBody();
                while ((line = reader.readLine()) != null) {
                    chatRespBody = GSON.fromJson(line, ChatRespBody.class);
                    content.append(chatRespBody.getMessage().getContent());
                }
                chatRespBody.getMessage().setContent(content.toString());
                return chatRespBody;
            } catch (IOException e) {
                throw BizException.of("对话请求读取流异常: ", e);
            } finally {
                try {
                    EntityUtils.consume(httpEntity);
                } catch (IOException e) {
                    log.error("对话请求关闭异常: ", e);
                }
            }
        } else {
            try {
                return GSON.fromJson(EntityUtils.toString(httpEntity), ChatRespBody.class);
            } catch (IOException e) {
                log.error("对话请求关闭异常: ", e);
                throw BizException.of("对话请求读取流异常: ", e);
            }
        }
    }
}
