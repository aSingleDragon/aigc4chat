package pers.hll.aigc4chat.base.gson;

import com.google.common.base.CaseFormat;
import com.google.gson.FieldNamingStrategy;
import pers.hll.aigc4chat.base.annotation.FromUnderscore;
import pers.hll.aigc4chat.base.annotation.ToUnderscore;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * 小驼峰和下划线命名策略
 *
 * @author hll
 * @since 2024/04/29
 */
public class Lca2UcFieldNamingStrategy implements FieldNamingStrategy {

    @Override
    public String translateName(Field field) {
        String name = field.getName();
        if (doesClazzHasAnnotation(field, ToUnderscore.class)) {
            return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name);
        }
        if (doesClazzHasAnnotation(field, FromUnderscore.class) && name.contains("_")) {
            return underscore2LowerCamelCase(name);
        }
        return name;
    }

    /**
     * 判断字段所属的类是否具有某个注解
     *
     * @param field           字段
     * @param annotationClass 注解类
     * @param <A>             注解类型
     * @return 是否具有该注解
     */
    private <A extends Annotation> boolean doesClazzHasAnnotation(Field field, Class<A> annotationClass) {
        return field.getDeclaringClass().getAnnotation(annotationClass) != null;
    }

    /**
     * 下划线转小驼峰
     *
     * @param name 字段名(下划线)
     * @return 变量名(小驼峰)
     */
    private String underscore2LowerCamelCase(String name) {
        StringBuilder camelCase = new StringBuilder();
        boolean upperCase = false;
        for (String part : name.split("_")) {
            if (upperCase) {
                camelCase.append(Character.toUpperCase(part.charAt(0)));
            } else {
                camelCase.append(Character.toLowerCase(part.charAt(0)));
                upperCase = true;
            }
            if (part.length() > 1) {
                camelCase.append(part.substring(1));
            }
        }
        return camelCase.toString();
    }
}