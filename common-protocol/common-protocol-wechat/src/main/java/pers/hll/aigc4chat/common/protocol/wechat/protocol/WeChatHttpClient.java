package pers.hll.aigc4chat.common.protocol.wechat.protocol;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.request.BaseRequest;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

/**
 *
 * @author hll
 * @since 2024/03/11
 */
@Slf4j
@UtilityClass
public class WeChatHttpClient {

    public void post(BaseRequest baseRequest) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(baseRequest.getUri());
            httpPost.setHeader("Content-Type", "application/json; charset=UTF-8");
            baseRequest.getHeaderMap().forEach(httpPost::setHeader);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T get(BaseRequest baseRequest) {
        RequestConfig requestConfig = RequestConfig
                .custom()
                .setRedirectsEnabled(baseRequest.isRedirectsEnabled())
                .build();
        try (CloseableHttpClient httpClient = HttpClients
                .custom()
                .setDefaultRequestConfig(requestConfig)
                .build()) {
            HttpGet httpGet = new HttpGet(baseRequest.getUri());
            baseRequest.getHeaderMap().forEach(httpGet::setHeader);
            URIBuilder uriBuilder = new URIBuilder(httpGet.getURI());
            baseRequest.getRequestParamMap().forEach(uriBuilder::addParameter);
            httpGet.setURI(uriBuilder.build());
            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                String strEntity = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8.name());
                return baseRequest.stringToGeneric(strEntity);
            }
        } catch (IOException | URISyntaxException e) {
            log.error("构造请求出错: ", e);
        }
        return null;
    }
}
