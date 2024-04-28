package pers.hll.aigc4chat.model.gemini;

import org.apache.http.HttpHost;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.auth.AuthScope;
import org.apache.http.util.EntityUtils;
import pers.hll.aigc4chat.base.util.XmlUtil;
import pers.hll.aigc4chat.base.xml.ProxyConfig;

import java.io.IOException;

/**
 * Http代理测试  随便写写 不要在意
 *
 * @author hll
 * @since 2024/04/26
 */
public class HttpClientProxyTest {
    public static void main(String[] args) throws IOException {
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        ProxyConfig proxyConfig = XmlUtil.readXmlConfig(ProxyConfig.class);
        credentialsProvider.setCredentials(
            new AuthScope(proxyConfig.getHost(), proxyConfig.getPort()),
            new UsernamePasswordCredentials(proxyConfig.getUsername(), proxyConfig.getPassword()));
        try (CloseableHttpClient httpclient = HttpClients.custom()
                .setDefaultCredentialsProvider(credentialsProvider)
                .build()) {
            HttpHost proxy = new HttpHost(proxyConfig.getHost(), proxyConfig.getPort());
            HttpGet httpGet = new HttpGet("https://google.com/");
            RequestConfig config = RequestConfig.custom()
                .setProxy(proxy)
                .build();
            httpGet.setConfig(config);
            try (CloseableHttpResponse response = httpclient.execute(httpGet)) {
                System.out.println(EntityUtils.toString(response.getEntity()));
            }
        }
    }
}
