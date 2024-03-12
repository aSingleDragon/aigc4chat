package pers.hll.aigc4chat.common.base.config;

import lombok.extern.slf4j.Slf4j;
import pers.hll.aigc4chat.common.base.config.configs.XConfigs;
import pers.hll.aigc4chat.common.base.config.configs.impl.XConfigsImpl;

import javax.annotation.Nonnull;
import java.io.IOException;

/**
 * 配置工具，此工具被其他依赖，所以此工具不能使用其他工具
 *
 * @author hll
 * @since 2023/03/10
 */
@Slf4j
public final class XConfigTools {

    public static final XConfigs X_CONFIGS = new XConfigsImpl();

    static {
        try {
            X_CONFIGS.cfgLoad("config.properties", "utf-8");
        } catch (IOException e) {
            log.error("读取默认配置文件[ config.properties ]失败");
        }
    }

    private XConfigTools() {
    }

    /**
     * 根据类名提供类的实例，自动判断是否为XSupplier，如果是，则调用其supply方法获取类的实例
     *
     * @param clazzName 类名
     * @param <T>       实例类型
     * @return 需求的类的实例
     */
    @Nonnull
    @SuppressWarnings("unchecked")
    public static <T> T supply(@Nonnull String clazzName) {
        try {
            Class<?> clazz = Class.forName(clazzName);
            if (XSupplier.class.isAssignableFrom(clazz)) {
                return ((XSupplier<T>) clazz.newInstance()).supply();
            } else {
                return (T) clazz.newInstance();
            }
        } catch (Exception e) {
            if (e instanceof ClassCastException classCastException) {
                log.error("使用[{}]获取实例时出错：类型不匹配", clazzName, classCastException);
                throw new ClassCastException(String.format("使用[ %s ]获取实例时出错：类型不匹配", clazzName));
            } else {
                log.error("使用[{}]获取实例时出错：无法实例化", clazzName, e);
                throw new IllegalArgumentException(String.format("使用[ %s ]获取实例时出错：无法实例化", clazzName));
            }
        }
    }
}
