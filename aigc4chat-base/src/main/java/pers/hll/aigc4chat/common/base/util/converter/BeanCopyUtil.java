package pers.hll.aigc4chat.common.base.util.converter;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author hll
 * @since 2023-08-24
 */
@Slf4j
@UtilityClass
public class BeanCopyUtil {

    /**
     * 输出属性复制代码
     * <p>假如有两个类SrcTest和TargetTest
     * <blockquote><pre>
     *     class SrcTest {
     *         private int a;
     *         private int b;
     *         private String c;
     *         // 省略getter setter 及构造方法
     *     }
     *
     *     class TargetTest {
     *         private int b;
     *         private String c;
     *         /// 省略getter setter 及构造方法
     *     }
     * </pre></blockquote></p>
     * <p>
     * 调用copyProperties后
     * <blockquote><pre>
     *     copyProperties(SrcTest.class, TargetTest.class, "a", "b");
     * </pre></blockquote>
     * </p>
     * <p>输出
     * <blockquote><pre>
     *     b.setC(a.getC());
     *     b.setB(a.getB());
     * </pre></blockquote>
     * </p>
     *
     * @param sourceClass          源类元
     * @param targetClass          目标类元
     * @param sourceVarName        源对象变量名
     * @param targetVarName        目标对象变量名
     * @param withSuperClassFields 是否包含父类字段
     */
    public List<String> copyProperties(Class<?> sourceClass, Class<?> targetClass, String sourceVarName,
                                       String targetVarName, boolean withSuperClassFields) {

        Set<String> srcSimpleFiledSet = getSimpleFiledSet(sourceClass, withSuperClassFields);
        Set<String> targetSimpleFiledSet = getSimpleFiledSet(targetClass, withSuperClassFields);
        List<String> out = new ArrayList<>();
        targetSimpleFiledSet.forEach(targetField -> {
            String fieldName = capitalizeSimpleField(targetField);
            if (srcSimpleFiledSet.contains(targetField)) {
                out.add(String.format("%s.set%s(%s.get%s());", targetVarName, fieldName, sourceVarName, fieldName));
            } else {
                out.add(String.format("// %s.set%s();", targetVarName, fieldName));
            }
        });
        return out;
    }

    /**
     * 实例变量名首字母大写
     *
     * @param simpleField 实例变量名 e
     */
    private String capitalizeSimpleField(String simpleField) {
        String[] split = simpleField.split(" ");
        return split[1].substring(0, 1).toUpperCase() + split[1].substring(1);
    }

    /**
     * @param clazz                类元
     * @param withSuperClassFields 是否包含父类字段
     * @return 字段信息
     */

    private Set<String> getSimpleFiledSet(Class<?> clazz, boolean withSuperClassFields) {
        return getFieldsDirectly(clazz, withSuperClassFields)
                .stream()
                .map(BeanCopyUtil::genKey)
                .collect(Collectors.toSet());
    }

    private String genKey(Field field) {
        String fieldToString = field.toString();
        String[] split = fieldToString.split(" ");
        String path = split[split.length - 1];
        path = path.substring(path.lastIndexOf(".") + 1);
        return String.join(" ", split[1], path);
    }

    /**
     * 获得一个类中所有字段列表，直接反射获取，无缓存<br>
     * 如果子类与父类中存在同名字段，则这两个字段同时存在，子类字段在前，父类字段在后。
     *
     * @param beanClass            类
     * @param withSuperClassFields 是否包括父类的字段列表
     * @return 字段列表
     * @throws SecurityException 安全检查异常
     */
    public List<Field> getFieldsDirectly(Class<?> beanClass, boolean withSuperClassFields) throws SecurityException {
        List<Field> allFields = null;
        Class<?> searchType = beanClass;
        List<Field> declaredFields;
        while (searchType != null) {
            declaredFields = Arrays.asList(searchType.getDeclaredFields());
            if (null == allFields) {
                allFields = declaredFields;
            } else {
                allFields.addAll(declaredFields);
            }
            searchType = withSuperClassFields ? searchType.getSuperclass() : null;
        }
        return allFields;
    }
}