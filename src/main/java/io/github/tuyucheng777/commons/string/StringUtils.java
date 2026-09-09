package io.github.tuyucheng777.commons.string;

import java.util.Objects;
import java.util.StringJoiner;

/// 字符串工具类，提供判空、空白判断、截断、连接等常用方法。
public final class StringUtils {

    private StringUtils() {
    }

    /// 判断字符串是否为 null 或空字符串。
    ///
    /// @param value 字符串
    /// @return 如果为空返回 true
    public static boolean isEmpty(String value) {
        return value == null || value.isEmpty();
    }

    /// 判断字符串是否非空。
    ///
    /// @param value 字符串
    /// @return 如果非空返回 true
    public static boolean isNotEmpty(String value) {
        return !isEmpty(value);
    }

    /// 判断字符串是否为 null、空字符串或只包含空白字符。
    ///
    /// @param value 字符串
    /// @return 如果为空白返回 true
    public static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    /// 判断字符串是否非空白。
    ///
    /// @param value 字符串
    /// @return 如果非空白返回 true
    public static boolean isNotBlank(String value) {
        return !isBlank(value);
    }

    /// 截断字符串，超过最大长度时追加后缀。
    ///
    /// @param value     原始字符串
    /// @param maxLength 最大长度
    /// @param suffix    后缀
    /// @return 截断后的字符串
    public static String truncate(String value, int maxLength, String suffix) {
        Objects.requireNonNull(suffix, "suffix must not be null");

        if (value == null) {
            return null;
        }

        if (maxLength < 0) {
            throw new IllegalArgumentException("maxLength must not be negative");
        }

        if (value.length() <= maxLength) {
            return value;
        }

        return value.substring(0, maxLength) + suffix;
    }

    /// 使用指定分隔符连接多个字符串。
    ///
    /// @param delimiter 分隔符
    /// @param values    字符串数组
    /// @return 连接后的字符串
    public static String join(String delimiter, String... values) {
        Objects.requireNonNull(delimiter, "delimiter must not be null");

        if (values == null || values.length == 0) {
            return "";
        }

        StringJoiner joiner = new StringJoiner(delimiter);

        for (String value : values) {
            if (value != null) {
                joiner.add(value);
            }
        }

        return joiner.toString();
    }

    /// 安全获取字符串，如果为 null 则返回默认值。
    ///
    /// @param value        原始字符串
    /// @param defaultValue 默认值
    /// @return 非空字符串
    public static String defaultIfNull(String value, String defaultValue) {
        return value == null ? defaultValue : value;
    }
}