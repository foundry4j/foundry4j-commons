package io.github.tuyucheng777.commons.stream;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/// Stream 工具类，提供过滤、映射、去重、收集等常用方法。
public final class StreamUtils {

    private StreamUtils() {
    }

    /// 对流进行过滤并收集为列表。
    ///
    /// @param stream    原始流
    /// @param predicate 过滤条件
    /// @param <T>       元素类型
    /// @return 过滤后的列表
    public static <T> List<T> filterToList(Stream<T> stream, Predicate<? super T> predicate) {
        Objects.requireNonNull(stream, "stream must not be null");
        Objects.requireNonNull(predicate, "predicate must not be null");

        return stream.filter(predicate).collect(Collectors.toList());
    }

    /// 对流进行映射并收集为列表。
    ///
    /// @param stream 原始流
    /// @param mapper 映射函数
    /// @param <T>    原始元素类型
    /// @param <R>    映射后元素类型
    /// @return 映射后的列表
    public static <T, R> List<R> mapToList(Stream<T> stream, Function<? super T, ? extends R> mapper) {
        Objects.requireNonNull(stream, "stream must not be null");
        Objects.requireNonNull(mapper, "mapper must not be null");

        return stream.map(mapper).collect(Collectors.toList());
    }

    /// 对流进行去重并收集为列表，保持原始顺序。
    ///
    /// @param stream 原始流
    /// @param <T>    元素类型
    /// @return 去重后的列表
    public static <T> List<T> distinctToList(Stream<T> stream) {
        Objects.requireNonNull(stream, "stream must not be null");

        return stream.distinct().collect(Collectors.toList());
    }

    /// 对流进行去重并收集为集合。
    ///
    /// @param stream 原始流
    /// @param <T>    元素类型
    /// @return 去重后的集合
    public static <T> Set<T> distinctToSet(Stream<T> stream) {
        Objects.requireNonNull(stream, "stream must not be null");

        return stream.collect(Collectors.toCollection(LinkedHashSet::new));
    }
}