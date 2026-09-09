package io.github.tuyucheng777.commons.date;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/// 时间操作工具类，涵盖时分秒相关的常用方法。
public final class TimeUtils {

    public static final String DEFAULT_TIME_PATTERN = "HH:mm:ss";

    private TimeUtils() {
    }

    // ==================== Java 8 Time API ====================

    /// 获取当前时间。
    public static LocalTime now() {
        return LocalTime.now();
    }

    /// 解析时间字符串为 LocalTime。
    public static LocalTime parseTime(String timeStr) {
        return parseTime(timeStr, DEFAULT_TIME_PATTERN);
    }

    /// 按指定格式解析时间字符串。
    public static LocalTime parseTime(String timeStr, String pattern) {
        Objects.requireNonNull(timeStr, "timeStr must not be null");
        Objects.requireNonNull(pattern, "pattern must not be null");
        return LocalTime.parse(timeStr, DateTimeFormatter.ofPattern(pattern));
    }

    /// 格式化 LocalTime 为字符串。
    public static String formatTime(LocalTime time) {
        return formatTime(time, DEFAULT_TIME_PATTERN);
    }

    /// 按指定格式格式化 LocalTime。
    public static String formatTime(LocalTime time, String pattern) {
        Objects.requireNonNull(time, "time must not be null");
        Objects.requireNonNull(pattern, "pattern must not be null");
        return time.format(DateTimeFormatter.ofPattern(pattern));
    }

    /// 计算两个时间之间的秒数差。
    public static long secondsBetween(LocalTime start, LocalTime end) {
        Objects.requireNonNull(start, "start must not be null");
        Objects.requireNonNull(end, "end must not be null");
        return ChronoUnit.SECONDS.between(start, end);
    }

    /// 计算两个时间之间的 Duration。
    public static Duration durationBetween(LocalTime start, LocalTime end) {
        Objects.requireNonNull(start, "start must not be null");
        Objects.requireNonNull(end, "end must not be null");
        return Duration.between(start, end);
    }

    /// 时间加小时。
    public static LocalTime plusHours(LocalTime time, long hours) {
        Objects.requireNonNull(time, "time must not be null");
        return time.plusHours(hours);
    }

    /// 时间加分钟。
    public static LocalTime plusMinutes(LocalTime time, long minutes) {
        Objects.requireNonNull(time, "time must not be null");
        return time.plusMinutes(minutes);
    }

    /// 判断当前时间是否在指定范围内（包含起止）。
    public static boolean isBetween(LocalTime time, LocalTime start, LocalTime end) {
        Objects.requireNonNull(time, "time must not be null");
        Objects.requireNonNull(start, "start must not be null");
        Objects.requireNonNull(end, "end must not be null");
        return !time.isBefore(start) && !time.isAfter(end);
    }

    /// 判断是否为工作时间（9:00 - 18:00）。
    public static boolean isWorkTime(LocalTime time) {
        Objects.requireNonNull(time, "time must not be null");
        return isBetween(time, LocalTime.of(9, 0), LocalTime.of(18, 0));
    }

    /// 获取当前时间的小时数。
    public static int getHour(LocalTime time) {
        Objects.requireNonNull(time, "time must not be null");
        return time.getHour();
    }

    /// 获取当前时间的分钟数。
    public static int getMinute(LocalTime time) {
        Objects.requireNonNull(time, "time must not be null");
        return time.getMinute();
    }
}