package pers.hll.aigc4chat.common.base.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 常量工具类 这里的常量类 指的是符合Sonar规范的常量类
 *
 * @author hll
 * @since 2024/04/23
 */
@Slf4j
@UtilityClass
public class ConstantUtil {

    /**
     * 获取常量类中的字符串常量
     * <p>
     *     常量类应符合Sonar代码规范
     * </p>
     *
     * @param clazz 常量类
     * @return 常量类中的字符串常量
     */
    public List<String> listStringConstant(Class<?> clazz) {
        List<String> constValueList = new ArrayList<>();
        for (Field field : clazz.getFields()) {
            // 根据规范 我们认为 常量类的常量 应具有 统一的 public static final 修饰符
            if (java.lang.reflect.Modifier.isPublic(field.getModifiers())
                    && java.lang.reflect.Modifier.isStatic(field.getModifiers())
                    && java.lang.reflect.Modifier.isFinal(field.getModifiers())) {
                try {
                    Object constObjValue = field.get(null);
                    if (constObjValue instanceof String constStrValue) {
                        constValueList.add(constStrValue);
                    }
                } catch (IllegalAccessException e) {
                    log.error("获取常量类常量值失败", e);
                }
            }
        }
        return constValueList;
    }
}
