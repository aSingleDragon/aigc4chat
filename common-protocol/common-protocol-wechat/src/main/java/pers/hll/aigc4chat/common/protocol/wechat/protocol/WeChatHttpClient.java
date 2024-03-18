package pers.hll.aigc4chat.common.protocol.wechat.protocol;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.request.BasePostRequest;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.request.BaseRequest;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

/**
 * @author hll
 * @since 2024/03/11
 */
@Slf4j
@UtilityClass
public class WeChatHttpClient {

    private CookieStore cookieStore = null;

    public <RequestType, ResponseType> ResponseType post(BasePostRequest<RequestType, ResponseType> basePostRequest) {

        RequestConfig requestConfig = RequestConfig
                .custom()
                .setConnectTimeout(10 * 1000)
                .setSocketTimeout(60 * 1000)
                .build();

        try (CloseableHttpClient httpClient = HttpClients
                .custom()
                .setDefaultRequestConfig(requestConfig)
                .build()) {

            HttpPost httpPost = new HttpPost(basePostRequest.getUri());
            basePostRequest.getHeaderMap().forEach(httpPost::setHeader);

            URIBuilder uriBuilder = new URIBuilder(httpPost.getURI());
            basePostRequest.getRequestParamMap().forEach((key, value) -> uriBuilder.addParameter(key, value.toString()));
            httpPost.setURI(uriBuilder.build());
            httpPost.setEntity(new StringEntity(basePostRequest.buildRequestBody(), ContentType.APPLICATION_JSON));

            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                String strEntity = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8.name());
                return basePostRequest.convertRespBodyToObj(strEntity);
            }
        } catch (IOException | URISyntaxException e) {
            log.error("构造Post请求出错: ", e);
        }
        return null;
    }

    public <RequestType, ResponseType> ResponseType get(BaseRequest<RequestType, ResponseType> baseRequest) {

        RequestConfig requestConfig = RequestConfig
                .custom()
                .setConnectTimeout(10 * 1000)
                .setSocketTimeout(60 * 1000)
                .setRedirectsEnabled(baseRequest.isRedirectsEnabled())
                .build();

        try (CloseableHttpClient httpClient = HttpClients
                .custom()
                .setDefaultCookieStore(cookieStore)
                .setDefaultRequestConfig(requestConfig)
                .build()) {

            HttpGet httpGet = new HttpGet(baseRequest.getUri());
            baseRequest.getHeaderMap().forEach(httpGet::setHeader);

            URIBuilder uriBuilder = new URIBuilder(httpGet.getURI());
            baseRequest.getRequestParamMap().forEach((key, value) -> uriBuilder.addParameter(key, value.toString()));
            httpGet.setURI(uriBuilder.build());

            HttpContext context = HttpClientContext.create();
            context.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);

            try (CloseableHttpResponse response = httpClient.execute(httpGet, context)) {
                if (baseRequest.isFileStreamAvailable()) {
                    baseRequest.setInputStream(response.getEntity().getContent());
                } else {
                    String strEntity = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8.name());
                    cookieStore = (CookieStore) context.getAttribute(HttpClientContext.COOKIE_STORE);
                    return baseRequest.convertRespBodyToObj(strEntity);
                }
            }
        } catch (IOException | URISyntaxException e) {
            log.error("构造Get请求出错: ", e);
        }
        return null;
    }


    public static void main(String[] args) throws IOException {
        // 创建一个Cookie存储对象
        CookieStore cookieStore = new BasicCookieStore();
        // 添加一个Cookie到CookieStore中
        BasicClientCookie cookie = new BasicClientCookie("cookieName", "cookieValue");
        cookie.setDomain("yourdomain.com");  // 设置域名
        cookie.setPath("/");                 // 设置路径（可选）
        cookieStore.addCookie(cookie);
        // 创建HttpClient实例并设置默认的CookieStore
        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultCookieStore(cookieStore)
                .build();
        // 创建请求配置
        RequestConfig requestConfig = RequestConfig.custom().build();
        // 创建HttpGet请求
        HttpGet httpGet = new HttpGet("http://yourdomain.com/some-resource");
        httpGet.setConfig(requestConfig);
        // 获取HttpContext实例以关联CookieStore
        HttpContext context = HttpClientContext.create();
        context.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);
        // 执行HTTP GET请求
        CloseableHttpResponse response1 = httpClient.execute(httpGet, context);
        // 处理响应...
        // ...
        // 下一次请求时，不需要重新添加Cookie，直接使用同一个HttpClient实例和上下文即可
        HttpGet nextRequest = new HttpGet("http://yourdomain.com/another-resource");
        nextRequest.setConfig(requestConfig);
        // 使用相同的context执行下一个请求
        CloseableHttpResponse response2 = httpClient.execute(nextRequest, context);
        // ...处理response2...
        // 最后关闭HTTP客户端
        response1.close();
        response2.close();
        httpClient.close();
    }
}
