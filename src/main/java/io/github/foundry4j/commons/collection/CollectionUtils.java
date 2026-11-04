package io.github.foundry4j.commons.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/// 集合工具类，提供判空、转换、分组、分区等常用方法。
public final class CollectionUtils {

    private CollectionUtils() {
    }

    /// 判断集合是否为 null 或空。
    ///
    /// @param collection 集合
    /// @return 如果为空返回 true
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /// 判断集合是否非空。
    ///
    /// @param collection 集合
    /// @return 如果非空返回 true
    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    /// 将集合转换为不可变列表。
    ///
    /// @param collection 原始集合
    /// @param <T>        元素类型
    /// @return 不可变列表
    public static <T> List<T> toUnmodifiableList(Collection<T> collection) {
        if (collection == null) {
            return List.of();
        }
        return List.copyOf(collection);
    }

    /// 按指定键提取器对集合进行分组。
    ///
    /// @param collection   原始集合
    /// @param keyExtractor 键提取器
    /// @param <K>          键类型
    /// @param <V>          值类型
    /// @return 分组结果
    public static <K, V> Map<K, List<V>> groupBy(Collection<V> collection, Function<? super V, ? extends K> keyExtractor) {
        Objects.requireNonNull(keyExtractor, "keyExtractor must not be null");

        Map<K, List<V>> result = new LinkedHashMap<>();

        if (collection == null) {
            return result;
        }

        for (V value : collection) {
            K key = keyExtractor.apply(value);
            result.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
        }

        return Collections.unmodifiableMap(result);
    }

    /// 将列表按指定大小分区。
    ///
    /// @param list 原始列表
    /// @param size 分区大小
    /// @param <T>  元素类型
    /// @return 分区后的列表
    public static <T> List<List<T>> partition(List<T> list, int size) {
        Objects.requireNonNull(list, "list must not be null");

        if (size <= 0) {
            throw new IllegalArgumentException("partition size must be greater than 0");
        }

        List<List<T>> partitions = new ArrayList<>();

        for (int i = 0; i < list.size(); i += size) {
            partitions.add(List.copyOf(list.subList(i, Math.min(i + size, list.size()))));
        }

        return List.copyOf(partitions);
    }
}