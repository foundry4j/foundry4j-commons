package io.github.foundry4j.commons.date;

import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DateTimeFormatterUtilsTest {

    @Test
    void formatDateTime_defaultPattern() {
        LocalDateTime dt = LocalDateTime.of(2026, 9, 9, 16, 30, 45);
        assertEquals("2026-09-09 16:30:45", DateTimeFormatterUtils.formatDateTime(dt));
    }

    @Test
    void formatDateTime_customPattern() {
        LocalDateTime dt = LocalDateTime.of(2026, 9, 9, 16, 30, 45);
        assertEquals("20260909163045", DateTimeFormatterUtils.formatDateTime(dt, DateTimeFormatterUtils.PATTERN_DATETIME_COMPACT));
    }

    @Test
    void parseDateTime_defaultPattern() {
        LocalDateTime dt = DateTimeFormatterUtils.parseDateTime("2026-09-09 16:30:45");
        assertEquals(LocalDateTime.of(2026, 9, 9, 16, 30, 45), dt);
    }

    @Test
    void parseDateTime_customPattern() {
        LocalDateTime dt = DateTimeFormatterUtils.parseDateTime("20260909163045", DateTimeFormatterUtils.PATTERN_DATETIME_COMPACT);
        assertEquals(LocalDateTime.of(2026, 9, 9, 16, 30, 45), dt);
    }

    @Test
    void formatLegacyDate_shouldFormatCorrectly() {
        Date date = Date.from(LocalDateTime.of(2026, 9, 9, 16, 30, 45).atZone(ZoneId.systemDefault()).toInstant());
        String result = DateTimeFormatterUtils.formatLegacyDate(date, "yyyy-MM-dd HH:mm:ss");
        assertEquals("2026-09-09 16:30:45", result);
    }

    @Test
    void parseLegacyDate_shouldParseCorrectly() throws ParseException {
        Date date = DateTimeFormatterUtils.parseLegacyDate("2026-09-09 16:30:45", "yyyy-MM-dd HH:mm:ss");
        assertNotNull(date);
    }

    @Test
    void toZonedDateTime_shouldConvertCorrectly() {
        LocalDateTime dt = LocalDateTime.of(2026, 9, 9, 16, 30, 45);
        ZonedDateTime zdt = DateTimeFormatterUtils.toZonedDateTime(dt, "Asia/Shanghai");
        assertEquals(ZoneId.of("Asia/Shanghai"), zdt.getZone());
        assertEquals(dt, zdt.toLocalDateTime());
    }

    @Test
    void nowCompact_shouldReturnCompactFormat() {
        String compact = DateTimeFormatterUtils.nowCompact();
        assertNotNull(compact);
        // 格式为 yyyyMMddHHmmss，长度应为 14
        assertEquals(14, compact.length());
        assertTrue(compact.matches("\\d{14}"));
    }

    @Test
    void nowIso_shouldReturnIsoFormat() {
        String iso = DateTimeFormatterUtils.nowIso();
        assertNotNull(iso);
        // 格式为 yyyy-MM-dd'T'HH:mm:ss，长度应为 19
        assertEquals(19, iso.length());
        assertTrue(iso.contains("T"));
    }

    @Test
    void formatDateTime_shouldThrowOnNull() {
        assertThrows(NullPointerException.class, () -> DateTimeFormatterUtils.formatDateTime(null));
    }
}