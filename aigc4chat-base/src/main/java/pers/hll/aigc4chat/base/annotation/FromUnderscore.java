package pers.hll.aigc4chat.base.annotation;

import pers.hll.aigc4chat.base.gson.Lca2UcFieldNamingStrategy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记型注解
 * <p>标记在将一个Json字符串转换为这个类(根据Java规范, 默认字段命名皆为小驼峰风格)时
 * 将Json的下划线风格映射到类的小驼峰风格
 * 使用{@link com.google.gson.Gson}进行序列化
 *
 * @author hll 
 * @since 2024/04/29
 * @see Lca2UcFieldNamingStrategy
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface FromUnderscore {}