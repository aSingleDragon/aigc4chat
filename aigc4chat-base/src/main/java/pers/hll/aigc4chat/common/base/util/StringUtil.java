package pers.hll.aigc4chat.common.base.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * String 工具类
 *
 * @author hll
 * @since 2024/04/05
 */
@Slf4j
@UtilityClass
public class StringUtil {

    /**
     * 按指定分隔符将字符串拆分成数字集合
     *
     * @param str       被拆分的字符串
     * @param separator 分隔符
     * @return 数字集合
     */
    public List<Long> splitToLong(String str, String separator) {
        return Arrays.stream(str.split(separator))
                .map(String::trim)
                .filter(StringUtils::isNotBlank)
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }

    /**
     * 按指定分隔符将字符串拆分成指定类型集合
     * <blockquote><pre>
     *     String goodCodes = "A,B,C,D";
     *     List&lt;String&gt; goodCodeList =
     *       StringUtil.splitToList(goodsCodes, ",", String::valueOf);
     * </pre></blockquote>
     *
     * @param str       被拆分的字符串
     * @param separator 分隔符
     * @param mapper    转换函数
     * @return R类型集合
     */
    public <R> List<R> splitToList(String str, String separator, Function<? super String, ? extends R> mapper) {
        return Arrays.stream(str.split(separator))
                .map(String::trim)
                .filter(StringUtils::isNotBlank)
                .map(mapper)
                .collect(Collectors.toList());
    }

    /**
     * 按默认分隔符 "," 将字符串拆分成指定类型集合
     * <blockquote><pre>
     *     String goodCodes = "A,B,C,D";
     *     List&lt;String&gt; goodCodeList =
     *       StringUtil.splitToList(goodsCodes, String::valueOf);
     * </pre></blockquote>
     *
     * @param str       被拆分的字符串
     * @param mapper    转换函数
     * @return R类型集合
     */
    public <R> List<R> splitToList(String str, Function<? super String, ? extends R> mapper) {
        return splitToList(str, ",", mapper);
    }

    public List<String> splitToList(String str) {
        return splitToList(str, ",", String::valueOf);
    }

    public boolean notNullOrEmpty(Object obj) {
        if (obj instanceof CharSequence cs) {
            return StringUtils.isNotEmpty(cs);
        }
        return Objects.nonNull(obj);
    }
}
