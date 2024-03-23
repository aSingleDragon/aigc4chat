package pers.hll.aigc4chat.common.protocol.wechat.protocol;

import lombok.Data;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.request.BasePostRequest;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.request.BaseRequest;
import pers.hll.aigc4chat.common.protocol.wechat.protocol.request.form.FormFile;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

/**
 * 微信HTTP客户端
 *
 * @author hll
 * @since 2024/03/11
 */
@Slf4j
@UtilityClass
public class WeChatHttpClient {

    /**
     * 复用Cookie信息
     */
    @Getter
    private CookieStore cookieStore = null;

    public <RequestType, ResponseType> ResponseType post(BasePostRequest<RequestType, ResponseType> basePostRequest) {

        RequestConfig requestConfig = RequestConfig
                .custom()
                .setConnectTimeout(10 * 1000)
                .setSocketTimeout(60 * 1000)
                .build();

        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();

        if (!basePostRequest.getFormText().isEmpty()) {
            basePostRequest.getFormText().forEach(
                    (key, value) -> multipartEntityBuilder.addTextBody(key, value, ContentType.TEXT_PLAIN));
        }

        FormFile formFile = basePostRequest.getFormFile();
        if (formFile != null ) {
            multipartEntityBuilder.addBinaryBody(
                    formFile.formKey(), formFile.fileBytes(), formFile.contentType() , formFile.fileName());
        }

        try (CloseableHttpClient httpClient = HttpClients
                .custom()
                .setDefaultCookieStore(cookieStore)
                .setDefaultRequestConfig(requestConfig)
                .build()) {

            HttpPost httpPost = new HttpPost(basePostRequest.getUri());
            basePostRequest.getHeaderMap().forEach(httpPost::setHeader);

            URIBuilder uriBuilder = new URIBuilder(httpPost.getURI());
            basePostRequest.getRequestParamMap().forEach((key, value) -> uriBuilder.addParameter(key, value.toString()));
            httpPost.setURI(uriBuilder.build());

            if (basePostRequest.buildRequestBody() != null) {
                httpPost.setEntity(new StringEntity(basePostRequest.buildRequestBody(), ContentType.APPLICATION_JSON));
            } else {
                httpPost.setEntity(multipartEntityBuilder.build());
            }

            HttpContext context = HttpClientContext.create();
            context.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);

            try (CloseableHttpResponse response = httpClient.execute(httpPost, context)) {
                String strEntity = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8.name());
                cookieStore = (CookieStore) context.getAttribute(HttpClientContext.COOKIE_STORE);
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
                    File file = new File(baseRequest.getFileStreamSavePath());
                    FileUtils.copyInputStreamToFile(response.getEntity().getContent(), file);
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
}
