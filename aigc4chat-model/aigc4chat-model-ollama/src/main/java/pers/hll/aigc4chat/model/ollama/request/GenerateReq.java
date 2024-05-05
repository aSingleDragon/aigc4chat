package pers.hll.aigc4chat.model.ollama.request;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import pers.hll.aigc4chat.base.exception.BizException;
import pers.hll.aigc4chat.model.ollama.request.body.GenerateReqBody;
import pers.hll.aigc4chat.model.ollama.response.body.GenerateRespBody;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 *
 * @author hll
 * @since 2024/05/01
 */
@Slf4j
public class GenerateReq extends BasePostRequest<GenerateReqBody<?>, GenerateRespBody> {

    public GenerateReq(String uri) {
        super(uri);
    }

    @Override
    public GenerateRespBody convertHttpEntityToObj(HttpEntity httpEntity) {
        if (getRequestBody().getStream() == null || getRequestBody().getStream()) {
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(httpEntity.getContent(), StandardCharsets.UTF_8))) {
                String line;
                StringBuilder response = new StringBuilder();
                GenerateRespBody generateRespBody = new GenerateRespBody();
                while ((line = reader.readLine()) != null) {
                    generateRespBody = GSON.fromJson(line, GenerateRespBody.class);
                    response.append(generateRespBody.getResponse());
                }
                generateRespBody.setResponse(response.toString());
                return generateRespBody;
            } catch (IOException e) {
                throw BizException.of("生成请求读取流异常: ", e);
            } finally {
                try {
                    EntityUtils.consume(httpEntity);
                } catch (IOException e) {
                    log.error("生成请求关闭异常: ", e);
                }
            }
        } else {
            try {
                return GSON.fromJson(EntityUtils.toString(httpEntity), GenerateRespBody.class);
            } catch (IOException e) {
                log.error("生成请求关闭异常: ", e);
                throw BizException.of("生成请求读取流异常: ", e);
            }
        }
    }
}
