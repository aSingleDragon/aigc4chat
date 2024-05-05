package pers.hll.aigc4chat.model.ollama.request;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import pers.hll.aigc4chat.base.exception.BizException;
import pers.hll.aigc4chat.model.ollama.request.body.ShowReqBody;
import pers.hll.aigc4chat.model.ollama.response.body.ShowRespBody;

import java.io.IOException;

/**
 * 模型详情请求
 *
 * @author hll
 * @since 2024/05/04
 */
@Slf4j
public class ShowReq extends BasePostRequest<ShowReqBody, ShowRespBody> {

    public ShowReq(String uri) {
        super(uri);
    }

    @Override
    public ShowRespBody convertHttpEntityToObj(HttpEntity httpEntity) {
        try {
            return GSON.fromJson(EntityUtils.toString(httpEntity), ShowRespBody.class);
        } catch (IOException e) {
            log.error("模型详情转换异常: ", e);
            throw BizException.of("模型详情转换异常: ", e);
        }
    }
}
