package pers.lys.aigc4chat.common.ai;

import pers.lys.aigc4chat.common.ai.enums.ReplyModeEnum;
import pers.lys.aigc4chat.common.ai.mod.AgentMod;
import pers.lys.aigc4chat.common.ai.mod.ErnieMod;

/**
 * AI模型调用接口
 *
 * @author LiYaosheng
 * @since 2024/3/26
 */
public class AiModCallServiceImpl {

    /**
     * AI 对话请求
     *
     * @param content  提问
     * @param modCode  AI模型编码
     * @return 回答
     */
    public String call(String content, String modCode) {

        String message = null;
        if(ReplyModeEnum.ERNIE.getCode().equals(modCode)){
            ErnieMod ernieMod = new ErnieMod();
            message = ernieMod.getMessage(content);
        }
        if(ReplyModeEnum.AGENT.getCode().equals(modCode)){
            AgentMod agentMod = new AgentMod();
            message = agentMod.getMessage(content);
        }
        // TODO 复读机模式

        return message;
    }
}
