package pers.lys.aigc4chat.common.ai.mod;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.entity.ContentType;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import pers.hll.aigc4chat.common.base.util.BaseUtil;
import pers.lys.aigc4chat.common.ai.entity.ernie.ErnieResp;
import pers.lys.aigc4chat.common.ai.entity.ernie.ErnieReq;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 文心一言 ERNIE-Lite-8K-0922模型请求
 *
 * @author LiYaosheng
 * @since 2024/3/22
 */
@Slf4j
public class ErnieMod extends BaseModCall{

    private static final String URL = "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/eb-instant";

    private static final String TOKEN = "24.838a442771fe4b6040844f4df01e4ef0.2592000.1713537604.282335-55658556";

    @Override
    public  String getMessage(String content){
        ErnieReq ernieReq = new ErnieReq();
        ErnieReq.Message message = new ErnieReq.Message();
        message.setRole("user");
        message.setContent(content);
        ernieReq.setMessages(Collections.singletonList(message));

        Map<String,String> param = new HashMap<>(4);
        param.put("access_token", TOKEN);
        String strEntity = respStr(ernieReq,URL,null,param);
        ErnieResp resp = BaseUtil.GSON.fromJson(strEntity, ErnieResp.class);
        return resp.getResult();
    }
}
