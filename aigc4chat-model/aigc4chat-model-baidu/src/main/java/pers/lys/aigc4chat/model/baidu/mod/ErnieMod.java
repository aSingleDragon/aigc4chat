package pers.lys.aigc4chat.model.baidu.mod;

import lombok.extern.slf4j.Slf4j;
import pers.hll.aigc4chat.base.util.BaseUtil;
import pers.lys.aigc4chat.model.baidu.entity.ernie.ErnieResp;
import pers.lys.aigc4chat.model.baidu.entity.ernie.ErnieReq;

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
