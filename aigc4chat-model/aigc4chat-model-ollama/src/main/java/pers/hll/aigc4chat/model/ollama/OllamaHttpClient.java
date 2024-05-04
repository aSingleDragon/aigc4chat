package pers.hll.aigc4chat.model.ollama;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import pers.hll.aigc4chat.base.exception.BizException;
import pers.hll.aigc4chat.model.ollama.request.BasePostRequest;
import pers.hll.aigc4chat.model.ollama.request.BaseRequest;

import java.io.IOException;

/**
 * OllamaHTTP客户端
 *
 * @author hll
 * @since 2024/03/11
 */
@Slf4j
@UtilityClass
public class OllamaHttpClient {

    public <Q, R> R post(BasePostRequest<Q, R> basePostRequest) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(basePostRequest.getUri());
            httpPost.setEntity(new StringEntity(basePostRequest.buildRequestBody(), ContentType.APPLICATION_JSON));
            try (CloseableHttpResponse response = client.execute(httpPost)) {
                return basePostRequest.convertHttpEntityToObj(response.getEntity());
            }
        } catch (IOException e) {
            log.error("Ollama 接口调用异常: ", e);
            throw BizException.of("Ollama 接口调用异常", e);
        }
    }

    public <R> R get(BaseRequest<R> baseRequest) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet httpPost = new HttpGet(baseRequest.getUri());
            try (CloseableHttpResponse response = client.execute(httpPost)) {
                return baseRequest.convertHttpEntityToObj(response.getEntity());
            }
        } catch (IOException e) {
            log.error("Ollama[{}]接口调用异常: ", baseRequest.getUri(), e);
            throw BizException.of("Ollama[{}]接口调用异常: ", baseRequest.getUri(), e);
        }
    }
}
