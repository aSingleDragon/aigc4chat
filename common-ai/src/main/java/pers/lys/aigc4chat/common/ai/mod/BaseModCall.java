package pers.lys.aigc4chat.common.ai.mod;

import com.google.gson.JsonSyntaxException;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.poi.ss.formula.functions.T;
import pers.hll.aigc4chat.common.base.util.BaseUtil;
import pers.lys.aigc4chat.common.ai.entity.agent.AgentReq;
import pers.lys.aigc4chat.common.ai.entity.agent.AgentResp;
import pers.lys.aigc4chat.common.ai.entity.ernie.ErnieReq;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

/**
 * @author LiYaosheng
 * @since 2024/3/26
 */
@Slf4j
public class BaseModCall {

    public  String getMessage(String content) {
        return content;
    }

    public String respStr(Object req, String url, Map<String,String> header, Map<String,String> param){
        RequestConfig requestConfig = RequestConfig
                .custom()
                .setConnectTimeout(10 * 1000)
                .setSocketTimeout(60 * 1000)
                .build();


        try (CloseableHttpClient httpClient = HttpClients
                .custom()
                .setDefaultCookieStore(null)
                .setDefaultRequestConfig(requestConfig)
                .build()) {

            // 构造http请求对象
            HttpPost httpPost = new HttpPost(url);
            URIBuilder uriBuilder = new URIBuilder(httpPost.getURI());
            //添加Query参数
            if(Objects.nonNull(param) && !param.isEmpty()) {
                param.forEach(uriBuilder::addParameter);
            }
            HttpContext context = HttpClientContext.create();
            httpPost.setURI(uriBuilder.build());
            //添加Header
            if(Objects.nonNull(header) && !header.isEmpty()) {
                header.forEach(httpPost::addHeader);
            }
            httpPost.setEntity(new StringEntity(BaseUtil.GSON.toJson(req), ContentType.APPLICATION_JSON));
            context.setAttribute(HttpClientContext.COOKIE_STORE, null);

            try (CloseableHttpResponse response = httpClient.execute(httpPost, context)) {
                return EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8.name());
            }
        }catch (IOException | JsonSyntaxException | URISyntaxException e) {
            log.error("构造Post请求出错: ", e);
        }

        return "对方不想理你，并且向你吐了口水！";
    }
}
