package pers.hll.aigc4chat.model.gemini;

import cn.hutool.extra.ssh.ChannelType;
import cn.hutool.extra.ssh.JschUtil;
import com.jcraft.jsch.ChannelDirectTCPIP;
import com.jcraft.jsch.Session;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.event.Level;

/**
 * Socks代理测试  随便写写 不要在意
 *
 * @author hll
 * @since 2024/04/26
 */
@Slf4j
public class SshTunnelHttpClient {

    private static final int LOCAL_PROXY_PORT = 1090;

    private static final String LOCAL_PROXY_HOST = "127.0.0.1";

    public static void main(String[] args) throws Exception {
        setupSshTunnel();
        // 构建HttpClient，指向本地SSH隧道端口作为代理
        HttpHost proxy = new HttpHost(LOCAL_PROXY_HOST, LOCAL_PROXY_PORT);
        try (CloseableHttpClient httpClient = HttpClients.custom()
                .setProxy(proxy)
                .build()) {
            HttpGet httpGet = new HttpGet("https://google.com");
            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                System.out.println(EntityUtils.toString(response.getEntity()));
            }
        }
    }

    private static void setupSshTunnel() throws Exception {
        log.isEnabledForLevel(Level.DEBUG);
        // FIXME 代理服务器连接补上 麻了

        Session session = JschUtil.getSession("47.240.24.82", 22, "root", "Imyour1st.papa");
        ChannelDirectTCPIP channel = (ChannelDirectTCPIP) session.openChannel(ChannelType.DIRECT_TCPIP.getValue());
        channel.setHost(LOCAL_PROXY_HOST);
        channel.setPort(LOCAL_PROXY_PORT);
        channel.setOrgIPAddress("47.240.24.82");
        channel.setOrgPort(1080);
        channel.connect();
        if (channel.isConnected()) {
            System.out.println("SSH Tunnel established");
        } else {
            System.out.println("SSH Tunnel failed");
        }
    }
}