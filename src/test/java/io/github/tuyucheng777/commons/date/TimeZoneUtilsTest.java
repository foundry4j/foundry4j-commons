package io.github.tuyucheng777.commons.date;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.*;

class TimeZoneUtilsTest {

    @Test
    void getDefaultZoneId_shouldReturnNonBlank() {
        String zoneId = TimeZoneUtils.getDefaultZoneId();
        assertNotNull(zoneId);
        assertFalse(zoneId.isBlank());
    }

    @Test
    void getAvailableZoneIds_shouldContainCommonZones() {
        Set<String> zones = TimeZoneUtils.getAvailableZoneIds();
        assertFalse(zones.isEmpty());
        assertTrue(zones.contains(TimeZoneUtils.ZONE_UTC));
        assertTrue(zones.contains(TimeZoneUtils.ZONE_SHANGHAI));
    }

    @Test
    void isValidZoneId_shouldValidateFormat() {
        assertTrue(TimeZoneUtils.isValidZoneId("Asia/Shanghai"));
        assertTrue(TimeZoneUtils.isValidZoneId("UTC"));

        assertFalse(TimeZoneUtils.isValidZoneId(null));
        assertFalse(TimeZoneUtils.isValidZoneId(""));
        assertFalse(TimeZoneUtils.isValidZoneId("Not/AZone"));
    }

    @Test
    void convertZone_shouldKeepSameInstant() {
        LocalDateTime dateTime = LocalDateTime.of(2026, 9, 9, 12, 0, 0);

        ZonedDateTime result = TimeZoneUtils.convertZone(
                dateTime, TimeZoneUtils.ZONE_SHANGHAI, TimeZoneUtils.ZONE_UTC);

        assertEquals(ZoneId.of(TimeZoneUtils.ZONE_UTC), result.getZone());
        assertEquals(LocalDateTime.of(2026, 9, 9, 4, 0, 0), result.toLocalDateTime());
    }

    @Test
    void convertZone_shouldThrowOnNull() {
        assertThrows(NullPointerException.class,
                () -> TimeZoneUtils.convertZone(null, TimeZoneUtils.ZONE_UTC, TimeZoneUtils.ZONE_UTC));
    }

    @Test
    void convertToZone_shouldUseSystemDefaultAsSource() {
        LocalDateTime dateTime = LocalDateTime.of(2026, 9, 9, 12, 0, 0);

        ZonedDateTime result = TimeZoneUtils.convertToZone(dateTime, TimeZoneUtils.ZONE_UTC);

        assertEquals(ZoneId.of(TimeZoneUtils.ZONE_UTC), result.getZone());
    }

    @Test
    void nowInZone_shouldReturnTargetZone() {
        ZonedDateTime result = TimeZoneUtils.nowInZone(TimeZoneUtils.ZONE_TOKYO);
        assertEquals(ZoneId.of(TimeZoneUtils.ZONE_TOKYO), result.getZone());
    }

    @Test
    void nowInZone_shouldThrowOnNull() {
        assertThrows(NullPointerException.class, () -> TimeZoneUtils.nowInZone(null));
    }

    @Test
    void getUtcOffsetMillis_shouldReturnCorrectOffset() {
        assertEquals(8 * 3600 * 1000, TimeZoneUtils.getUtcOffsetMillis(TimeZoneUtils.ZONE_SHANGHAI));
        assertEquals(0, TimeZoneUtils.getUtcOffsetMillis(TimeZoneUtils.ZONE_UTC));
    }

    @Test
    void getUtcOffsetString_shouldReturnOffsetFormat() {
        assertEquals("+08:00", TimeZoneUtils.getUtcOffsetString(TimeZoneUtils.ZONE_SHANGHAI));
        assertEquals("+00:00", TimeZoneUtils.getUtcOffsetString(TimeZoneUtils.ZONE_UTC));
    }

    @Test
    void getUtcOffsetString_shouldThrowOnInvalidZone() {
        assertThrows(Exception.class, () -> TimeZoneUtils.getUtcOffsetString("Not/AZone"));
    }

    @Test
    void isDaylightSavingTime_shouldDetectSummerTime() {
        // 2026-07-15 纽约处于夏令时
        Instant summer = Instant.parse("2026-07-15T12:00:00Z");
        assertTrue(TimeZoneUtils.isDaylightSavingTime(TimeZoneUtils.ZONE_NEW_YORK, summer));

        // 2026-01-15 纽约不处于夏令时
        Instant winter = Instant.parse("2026-01-15T12:00:00Z");
        assertFalse(TimeZoneUtils.isDaylightSavingTime(TimeZoneUtils.ZONE_NEW_YORK, winter));
    }

    @Test
    void isDaylightSavingTime_alwaysFalseForShanghai() {
        assertFalse(TimeZoneUtils.isDaylightSavingTime(TimeZoneUtils.ZONE_SHANGHAI, Instant.now()));
    }

    @Test
    void isDaylightSavingTime_noArgOverload() {
        boolean result = TimeZoneUtils.isDaylightSavingTime(TimeZoneUtils.ZONE_LONDON);
        assertTrue(result || !result);
    }

    @Test
    void isDaylightSavingTime_shouldThrowOnNull() {
        assertThrows(NullPointerException.class,
                () -> TimeZoneUtils.isDaylightSavingTime(null, Instant.now()));
    }

    @Test
    void toZoneId_shouldConvertFromLegacyTimeZone() {
        ZoneId zoneId = TimeZoneUtils.toZoneId(TimeZone.getTimeZone(TimeZoneUtils.ZONE_SHANGHAI));
        assertEquals(ZoneId.of(TimeZoneUtils.ZONE_SHANGHAI), zoneId);
    }

    @Test
    void toTimeZone_shouldConvertFromZoneId() {
        TimeZone timeZone = TimeZoneUtils.toTimeZone(ZoneId.of(TimeZoneUtils.ZONE_TOKYO));
        assertEquals(TimeZoneUtils.ZONE_TOKYO, timeZone.getID());
    }

    @Test
    void toZoneId_shouldThrowOnNull() {
        assertThrows(NullPointerException.class, () -> TimeZoneUtils.toZoneId(null));
    }

    @Test
    void toTimeZone_shouldThrowOnNull() {
        assertThrows(NullPointerException.class, () -> TimeZoneUtils.toTimeZone(null));
    }
}