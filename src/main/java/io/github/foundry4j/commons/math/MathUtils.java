package io.github.foundry4j.commons.math;

import java.util.Objects;

/// 数学工具类，提供求和、平均、最大、最小等常用方法。
public final class MathUtils {

    private MathUtils() {
    }

    /// 计算整数数组的总和。
    ///
    /// @param values 整数数组
    /// @return 总和
    public static long sum(int... values) {
        Objects.requireNonNull(values, "values must not be null");

        long total = 0L;

        for (int value : values) {
            total += value;
        }

        return total;
    }

    /// 计算整数数组的平均值。
    ///
    /// @param values 整数数组
    /// @return 平均值
    public static double average(int... values) {
        Objects.requireNonNull(values, "values must not be null");

        if (values.length == 0) {
            throw new IllegalArgumentException("values must not be empty");
        }

        return (double) sum(values) / values.length;
    }

    /// 计算整数数组中的最大值。
    ///
    /// @param values 整数数组
    /// @return 最大值
    public static int max(int... values) {
        Objects.requireNonNull(values, "values must not be null");

        if (values.length == 0) {
            throw new IllegalArgumentException("values must not be empty");
        }

        int max = values[0];

        for (int value : values) {
            if (value > max) {
                max = value;
            }
        }

        return max;
    }

    /// 计算整数数组中的最小值。
    ///
    /// @param values 整数数组
    /// @return 最小值
    public static int min(int... values) {
        Objects.requireNonNull(values, "values must not be null");

        if (values.length == 0) {
            throw new IllegalArgumentException("values must not be empty");
        }

        int min = values[0];

        for (int value : values) {
            if (value < min) {
                min = value;
            }
        }

        return min;
    }
}