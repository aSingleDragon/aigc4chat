package pers.hll.aigc4chat.common.protocol.wechat.protocol.response;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * <a href="https://github.com/eatmoreapple/openwechat/blob/master/base_response.go">Ret 解释来源</a>
 *
 *
 * @author hll
 * @author 2024/03/10
 */
@Data
public class BaseResponseBody {

    /**
     *  ticketError         Ret = -14  // ticket error
     * 	logicError          Ret = -2   // logic error
     * 	sysError            Ret = -1   // sys error
     * 	paramError          Ret = 1    // param error
     * 	failedLoginWarn     Ret = 1100 // failed login warn
     * 	failedLoginCheck    Ret = 1101 // failed login check
     * 	cookieInvalid       Ret = 1102 // cookie invalid
     * 	loginEnvAbnormality Ret = 1203 // login environmental abnormality
     * 	optTooOften         Ret = 1205 // operate too often
     */
    @SerializedName("Ret")
    public int ret;

    @SerializedName("ErrMsg")
    public String errMsg;
}