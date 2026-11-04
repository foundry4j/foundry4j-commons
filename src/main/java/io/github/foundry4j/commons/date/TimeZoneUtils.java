package io.github.foundry4j.commons.date;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.TimeZone;

/// 时区操作工具类。
public final class TimeZoneUtils {

    public static final String ZONE_SHANGHAI = "Asia/Shanghai";
    public static final String ZONE_UTC = "UTC";
    public static final String ZONE_NEW_YORK = "America/New_York";
    public static final String ZONE_TOKYO = "Asia/Tokyo";
    public static final String ZONE_LONDON = "Europe/London";

    private TimeZoneUtils() {
    }

    /// 获取系统默认时区 ID。
    public static String getDefaultZoneId() {
        return ZoneId.systemDefault().getId();
    }

    /// 获取所有可用时区 ID。
    public static Set<String> getAvailableZoneIds() {
        return ZoneId.getAvailableZoneIds();
    }

    /// 判断时区 ID 是否合法。
    public static boolean isValidZoneId(String zoneId) {
        if (zoneId == null || zoneId.isEmpty()) {
            return false;
        }
        try {
            ZoneId.of(zoneId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /// 将 LocalDateTime 从一个时区转换到另一个时区。
    public static ZonedDateTime convertZone(LocalDateTime dateTime, String fromZone, String toZone) {
        Objects.requireNonNull(dateTime, "dateTime must not be null");
        Objects.requireNonNull(fromZone, "fromZone must not be null");
        Objects.requireNonNull(toZone, "toZone must not be null");

        ZonedDateTime sourceZoned = dateTime.atZone(ZoneId.of(fromZone));
        return sourceZoned.withZoneSameInstant(ZoneId.of(toZone));
    }

    /// 将 LocalDateTime 从系统默认时区转换到指定时区。
    public static ZonedDateTime convertToZone(LocalDateTime dateTime, String toZone) {
        Objects.requireNonNull(dateTime, "dateTime must not be null");
        Objects.requireNonNull(toZone, "toZone must not be null");

        ZonedDateTime sourceZoned = dateTime.atZone(ZoneId.systemDefault());
        return sourceZoned.withZoneSameInstant(ZoneId.of(toZone));
    }

    /// 获取指定时区的当前时间。
    public static ZonedDateTime nowInZone(String zoneId) {
        Objects.requireNonNull(zoneId, "zoneId must not be null");
        return ZonedDateTime.now(ZoneId.of(zoneId));
    }

    /// 获取指定时区与 UTC 的偏移量（毫秒，旧式 API）。
    public static int getUtcOffsetMillis(String zoneId) {
        Objects.requireNonNull(zoneId, "zoneId must not be null");
        TimeZone tz = TimeZone.getTimeZone(zoneId);
        return tz.getRawOffset();
    }

    /// 获取指定时区与 UTC 的偏移量字符串（如 +08:00）。
    public static String getUtcOffsetString(String zoneId) {
        Objects.requireNonNull(zoneId, "zoneId must not be null");
        ZoneId zone = ZoneId.of(zoneId);
        ZoneOffset offset = zone.getRules().getOffset(java.time.Instant.now());
        // 零偏移时 ZoneOffset.toString() 返回 "Z"，这里统一转为 "+00:00"
        if (offset.getTotalSeconds() == 0) {
            return "+00:00";
        }
        return offset.toString();
    }

    /// 判断指定时区在指定时刻是否处于夏令时。
    public static boolean isDaylightSavingTime(String zoneId, java.time.Instant instant) {
        Objects.requireNonNull(zoneId, "zoneId must not be null");
        Objects.requireNonNull(instant, "instant must not be null");
        return ZoneId.of(zoneId).getRules().isDaylightSavings(instant);
    }

    /// 判断指定时区当前是否处于夏令时。
    public static boolean isDaylightSavingTime(String zoneId) {
        return isDaylightSavingTime(zoneId, java.time.Instant.now());
    }

    /// 将旧式 TimeZone 转为 ZoneId。
    public static ZoneId toZoneId(TimeZone timeZone) {
        Objects.requireNonNull(timeZone, "timeZone must not be null");
        return timeZone.toZoneId();
    }

    /// 将 ZoneId 转为旧式 TimeZone。
    public static TimeZone toTimeZone(ZoneId zoneId) {
        Objects.requireNonNull(zoneId, "zoneId must not be null");
        return TimeZone.getTimeZone(zoneId);
    }
}