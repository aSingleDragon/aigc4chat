package pers.hll.aigc4chat.base.util;

import lombok.experimental.UtilityClass;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 集合工具类
 *
 * @author hll
 * @since 2023/12/18
 */
@UtilityClass
public class EasyCollUtil {

    /**
     * 获取集合中某个字段的集合
     * <blockquote><pre>
     *     List&lt;Student&gt; studentList = ...
     *     List&lt;String&gt; nameList =
     *       EasyCollUtil.getFieldList(studentList, Student::getName);
     * </pre></blockquote>
     *
     * @param collection 集合
     * @param mapper     字段映射
     * @param <T>        集合元素类型
     * @param <R>        字段类型
     * @return 字段列表
     */
    public <T, R> List<R> getFieldList(Collection<T> collection, Function<? super T, ? extends R> mapper) {
        if (collection == null || collection.isEmpty()) {
            return Collections.emptyList();
        }
        return collection
                .stream()
                .map(mapper)
                .filter(StringUtil::notNullOrEmpty)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * 获取一个集合中实例对象某个字段到某个字段的的映射关系Map
     * <p>例如: 有一个Student集合，里面有id，name两个字段，我们需要将id和name映射到一个Map中，
     * 那么我们可以调用这个方法，传入两个字段映射函数，就可以得到一个Map</p>
     * <blockquote><pre>
     *     List&lt;Student&gt; studentList = ...
     *     Map&lt;Long, String&gt; studentIdNameMap =
     *       EasyCollUtil.getPairedFieldsMap(studentList, Student::getId, Student::getName);
     * </pre></blockquote>
     *
     * @param collection  集合
     * @param keyMapper   字段映射
     * @param valueMapper 字段映射
     * @param <T>         集合元素类型
     * @param <K>         字段类型
     * @param <U>         字段类型
     * @return 映射关系Map
     */
    public <T, K, U> Map<K, U> getPairedFieldsMap(Collection<T> collection, Function<? super T, ? extends K> keyMapper,
                                                         Function<? super T, ? extends U> valueMapper) {
        if (collection == null || collection.isEmpty()) {
            return Collections.emptyMap();
        }
        return collection
                .stream()
                .collect(Collectors.toMap(keyMapper, valueMapper, (oldValue, newValue) -> oldValue));
    }

    /**
     * 将集合转换为一个key为集合里实例对象的某个字段，value为集合里实例对象本身的Map
     * <blockquote><pre>
     *     List&lt;Student&gt; studentList = ...
     *     Map&lt;Long, Student&gt; studentIdIdentityMap =
     *       EasyCollUtil.toIdentityMap(studentList, Student::getId);
     * </pre></blockquote>
     *
     * @param collection 集合
     * @param mapper     字段映射
     * @param <T>        集合元素类型
     * @param <R>        字段类型
     * @return 映射关系Map
     */
    public <T, R> Map<R, T> toIdentityMap(Collection<T> collection, Function<? super T, ? extends R> mapper) {
        if (collection == null || collection.isEmpty()) {
            return Collections.emptyMap();
        }
        return collection
                .stream()
                .collect(Collectors.toMap(mapper, Function.identity(), (oldValue, newValue) -> oldValue));
    }

    /**
     * 将集合按照某个字段值进行分组得到一个Map
     * <br> e.g. 对学生集合按性别分组 key = 性别，value = 性别对应的学生集合
     * <blockquote><pre>
     *     List&lt;Student&gt; studentList = ...
     *     Map&lt;String, List&lt;Student&gt;&gt; studentGroupingByGenderMap =
     *       EasyCollUtil.toGroupingByMap(studentList, Student::getGender);
     * </pre></blockquote>
     *
     * @param collection 集合
     * @param mapper     字段映射
     * @param <T>        集合元素类型
     * @param <R>        字段类型
     * @return 映射关系Map
     */
    public <T, R> Map<R, List<T>> toGroupingByMap(Collection<T> collection, Function<? super T, ? extends R> mapper) {
        if (collection == null || collection.isEmpty()) {
            return Collections.emptyMap();
        }
        return collection
                .stream()
                .collect(Collectors.groupingBy(mapper));
    }
}
