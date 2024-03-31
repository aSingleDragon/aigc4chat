package pers.lys.aigc4chat.common.ai.mod;

import lombok.extern.slf4j.Slf4j;
import pers.hll.aigc4chat.common.base.util.BaseUtil;
import pers.lys.aigc4chat.common.ai.entity.agent.AgentReq;
import pers.lys.aigc4chat.common.ai.entity.agent.AgentResp;

import java.util.HashMap;
import java.util.Map;

/**
 * Agent自定义对话模型请求
 *
 * @author LiYaosheng
 * @since 2024/3/23
 */
@Slf4j
public class AgentMod extends BaseModCall{

    private static final String URL = "https://keyue.cloud.baidu.com/online/core/v5/block/query";

    private static final String TOKEN = "0c22783e-f390-4b61-b68e-605b26b3fd2d";

    @Override
    public String getMessage(String content){
        AgentReq agentReq = new AgentReq();
        agentReq.setQueryText(content);

        Map<String,String> header = new HashMap<>(4);
        header.put("token",TOKEN);
        String strEntity = respStr(agentReq,URL,header,null);
        AgentResp resp = BaseUtil.GSON.fromJson(strEntity, AgentResp.class);
        return resp.getData().getAnswer().get(0).getReply().getText();
    }
}
