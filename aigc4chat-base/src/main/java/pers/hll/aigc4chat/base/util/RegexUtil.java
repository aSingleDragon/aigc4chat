package pers.hll.aigc4chat.base.util;

import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式工具类
 *
 * @author hll
 * @since 2024/03/22
 */
@UtilityClass
public class RegexUtil {

    private final String REGEX_LOCATION = "coord=([^,]+),([^,]+)";

    public Location parseLocation(String url) {
        Pattern pattern = Pattern.compile(REGEX_LOCATION);
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            return new Location(matcher.group(1), matcher.group(2));
        }
        // "白宫", "宾夕法尼亚大道1600号"
        return new Location("-77.03678", "38.89752");
    }

    public record Location(String lon, String lat) {
    }
}

