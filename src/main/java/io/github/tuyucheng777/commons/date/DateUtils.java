package io.github.tuyucheng777.commons.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Objects;

/// 日期操作工具类，涵盖新旧日期 API 的常用方法。
public final class DateUtils {

    public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";

    private DateUtils() {
    }

    // ==================== Java 8 Date Time API ====================

    /// 获取今天的日期。
    public static LocalDate today() {
        return LocalDate.now();
    }

    /// 解析日期字符串为 LocalDate。
    public static LocalDate parseDate(String dateStr) {
        return parseDate(dateStr, DEFAULT_DATE_PATTERN);
    }

    /// 按指定格式解析日期字符串为 LocalDate。
    public static LocalDate parseDate(String dateStr, String pattern) {
        Objects.requireNonNull(dateStr, "dateStr must not be null");
        Objects.requireNonNull(pattern, "pattern must not be null");
        return LocalDate.parse(dateStr, java.time.format.DateTimeFormatter.ofPattern(pattern));
    }

    /// 将 LocalDate 格式化为字符串。
    public static String formatDate(LocalDate date) {
        return formatDate(date, DEFAULT_DATE_PATTERN);
    }

    /// 将 LocalDate 按指定格式化为字符串。
    public static String formatDate(LocalDate date, String pattern) {
        Objects.requireNonNull(date, "date must not be null");
        Objects.requireNonNull(pattern, "pattern must not be null");
        return date.format(java.time.format.DateTimeFormatter.ofPattern(pattern));
    }

    /// 日期加天数。
    public static LocalDate plusDays(LocalDate date, long days) {
        Objects.requireNonNull(date, "date must not be null");
        return date.plusDays(days);
    }

    /// 日期减天数。
    public static LocalDate minusDays(LocalDate date, long days) {
        Objects.requireNonNull(date, "date must not be null");
        return date.minusDays(days);
    }

    /// 计算两个日期之间的天数差。
    public static long daysBetween(LocalDate start, LocalDate end) {
        Objects.requireNonNull(start, "start must not be null");
        Objects.requireNonNull(end, "end must not be null");
        return ChronoUnit.DAYS.between(start, end);
    }

    /// 判断是否为闰年。
    public static boolean isLeapYear(int year) {
        return java.time.Year.isLeap(year);
    }

    /// 判断是否为闰年（基于 LocalDate）。
    public static boolean isLeapYear(LocalDate date) {
        Objects.requireNonNull(date, "date must not be null");
        return date.isLeapYear();
    }

    /// 判断目标日期是否在指定范围内（包含起止）。
    public static boolean isBetween(LocalDate date, LocalDate start, LocalDate end) {
        Objects.requireNonNull(date, "date must not be null");
        Objects.requireNonNull(start, "start must not be null");
        Objects.requireNonNull(end, "end must not be null");
        return !date.isBefore(start) && !date.isAfter(end);
    }

    /// 获取某月的天数。
    public static int daysInMonth(LocalDate date) {
        Objects.requireNonNull(date, "date must not be null");
        return date.lengthOfMonth();
    }

    /// 获取某年的天数。
    public static int daysInYear(LocalDate date) {
        Objects.requireNonNull(date, "date must not be null");
        return date.lengthOfYear();
    }

    // ==================== 旧式 Date API ====================

    /// 将旧式 Date 转为 LocalDate。
    public static LocalDate toLocalDate(Date date) {
        Objects.requireNonNull(date, "date must not be null");
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /// 将 LocalDate 转为旧式 Date。
    public static Date toDate(LocalDate localDate) {
        Objects.requireNonNull(localDate, "localDate must not be null");
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /// 用旧式 API 格式化 Date。
    public static String formatDate(Date date, String pattern) {
        Objects.requireNonNull(date, "date must not be null");
        Objects.requireNonNull(pattern, "pattern must not be null");
        return new SimpleDateFormat(pattern).format(date);
    }

    /// 用旧式 API 解析日期字符串为 Date。
    public static Date parseDateLegacy(String dateStr, String pattern) throws ParseException {
        Objects.requireNonNull(dateStr, "dateStr must not be null");
        Objects.requireNonNull(pattern, "pattern must not be null");
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        sdf.setLenient(false);
        return sdf.parse(dateStr);
    }

    /// 旧式 Date 加天数。
    public static Date plusDaysLegacy(Date date, int days) {
        Objects.requireNonNull(date, "date must not be null");
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(date);
        cal.add(java.util.Calendar.DAY_OF_MONTH, days);
        return cal.getTime();
    }
}