package pers.hll.aigc4chat.common.protocol.wechat.protocol;

import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.protocol.HttpContext;
import org.apache.http.impl.cookie.BasicClientCookie;

public class HttpClientSession {

    private CloseableHttpClient httpClient;

    private CookieStore cookieStore;

    public HttpClientSession() {
        // 创建一个Cookie存储对象
        cookieStore = new BasicCookieStore();
        // 使用这个CookieStore构建HttpClient实例
        httpClient = HttpClients.custom()
                .setDefaultCookieStore(cookieStore)
                .build();
    }

    public CloseableHttpResponse executeRequest(String url) throws Exception {
        // 创建请求配置（可选）
        RequestConfig requestConfig = RequestConfig.custom().build();

        // 创建HttpGet请求
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(requestConfig);

        // 获取HttpContext实例以关联CookieStore
        HttpContext context = HttpClientContext.create();
        context.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);

        // 执行HTTP GET请求
        CloseableHttpResponse response = httpClient.execute(httpGet, context);

        return response;
    }

    // 可以多次调用此方法，每次请求都会自动携带之前保存在CookieStore中的cookies
    public void sendMultipleRequests() throws Exception {
        String url1 = "http://example.com/endpoint1";
        CloseableHttpResponse response1 = executeRequest(url1);
        // ...处理response1...

        String url2 = "http://example.com/endpoint2";
        CloseableHttpResponse response2 = executeRequest(url2);
        // ...处理response2...
        
        // 关闭响应
        response1.close();
        response2.close();
    }
}