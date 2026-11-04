package io.github.foundry4j.commons.date;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;

/// 日期时间格式化/解析工具类，提供常用格式常量和快速格式化方法。
public final class DateTimeFormatterUtils {

    public static final String PATTERN_DATE = "yyyy-MM-dd";
    public static final String PATTERN_TIME = "HH:mm:ss";
    public static final String PATTERN_DATETIME = "yyyy-MM-dd HH:mm:ss";
    public static final String PATTERN_DATETIME_COMPACT = "yyyyMMddHHmmss";
    public static final String PATTERN_DATETIME_MILLIS = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String PATTERN_ISO_DATETIME = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String PATTERN_ISO_ZONED = "yyyy-MM-dd'T'HH:mm:ssXXX";

    private DateTimeFormatterUtils() {
    }

    /// 格式化 LocalDateTime 为默认格式 yyyy-MM-dd HH:mm:ss。
    public static String formatDateTime(LocalDateTime dateTime) {
        return formatDateTime(dateTime, PATTERN_DATETIME);
    }

    /// 按指定格式格式化 LocalDateTime。
    public static String formatDateTime(LocalDateTime dateTime, String pattern) {
        Objects.requireNonNull(dateTime, "dateTime must not be null");
        Objects.requireNonNull(pattern, "pattern must not be null");
        return dateTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    /// 解析日期时间字符串为 LocalDateTime。
    public static LocalDateTime parseDateTime(String dateTimeStr) {
        return parseDateTime(dateTimeStr, PATTERN_DATETIME);
    }

    /// 按指定格式解析日期时间字符串。
    public static LocalDateTime parseDateTime(String dateTimeStr, String pattern) {
        Objects.requireNonNull(dateTimeStr, "dateTimeStr must not be null");
        Objects.requireNonNull(pattern, "pattern must not be null");
        return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern(pattern));
    }

    /// 格式化旧式 Date 为字符串。
    public static String formatLegacyDate(Date date, String pattern) {
        Objects.requireNonNull(date, "date must not be null");
        Objects.requireNonNull(pattern, "pattern must not be null");
        return new java.text.SimpleDateFormat(pattern).format(date);
    }

    /// 用旧式 API 解析日期时间字符串。
    public static Date parseLegacyDate(String dateStr, String pattern) throws java.text.ParseException {
        Objects.requireNonNull(dateStr, "dateStr must not be null");
        Objects.requireNonNull(pattern, "pattern must not be null");
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(pattern);
        sdf.setLenient(false);
        return sdf.parse(dateStr);
    }

    /// 将 LocalDateTime 转为带时区的 ZonedDateTime。
    public static ZonedDateTime toZonedDateTime(LocalDateTime dateTime, String zoneId) {
        Objects.requireNonNull(dateTime, "dateTime must not be null");
        Objects.requireNonNull(zoneId, "zoneId must not be null");
        return dateTime.atZone(ZoneId.of(zoneId));
    }

    /// 获取当前时间的紧凑格式字符串（如 20260909161717）。
    public static String nowCompact() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(PATTERN_DATETIME_COMPACT));
    }

    /// 获取当前时间的 ISO 格式字符串。
    public static String nowIso() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(PATTERN_ISO_DATETIME));
    }
}