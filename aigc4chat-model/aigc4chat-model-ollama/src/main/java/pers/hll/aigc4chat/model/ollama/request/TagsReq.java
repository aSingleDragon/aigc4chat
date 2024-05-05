package pers.hll.aigc4chat.model.ollama.request;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import pers.hll.aigc4chat.base.exception.BizException;
import pers.hll.aigc4chat.model.ollama.response.body.TagsRespBody;

import java.io.IOException;

/**
 * 本地支持的模型列表
 *
 * @author hll
 * @since 2024/05/04
 */
@Slf4j
public class TagsReq extends BaseRequest<TagsRespBody> {

    public TagsReq(String uri) {
        super(uri);
    }

    @Override
    public TagsRespBody convertHttpEntityToObj(HttpEntity httpEntity) {
        try {
            return GSON.fromJson(EntityUtils.toString(httpEntity), TagsRespBody.class);
        } catch (IOException e) {
            log.error("获取模型列表转换失败: ", e);
            throw BizException.of("获取模型列表失败: ", e);
        }
    }
}
