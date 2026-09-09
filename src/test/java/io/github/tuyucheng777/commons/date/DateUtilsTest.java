package io.github.tuyucheng777.commons.date;

import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class DateUtilsTest {

    @Test
    void today_shouldReturnCurrentDate() {
        LocalDate today = DateUtils.today();
        assertNotNull(today);
        assertEquals(LocalDate.now(), today);
    }

    @Test
    void parseDate_defaultPattern() {
        LocalDate date = DateUtils.parseDate("2026-09-09");
        assertEquals(LocalDate.of(2026, 9, 9), date);
    }

    @Test
    void parseDate_customPattern() {
        LocalDate date = DateUtils.parseDate("09/09/2026", "MM/dd/yyyy");
        assertEquals(LocalDate.of(2026, 9, 9), date);
    }

    @Test
    void formatDate_defaultPattern() {
        String result = DateUtils.formatDate(LocalDate.of(2026, 9, 9));
        assertEquals("2026-09-09", result);
    }

    @Test
    void formatDate_customPattern() {
        String result = DateUtils.formatDate(LocalDate.of(2026, 9, 9), "yyyyMMdd");
        assertEquals("20260909", result);
    }

    @Test
    void plusDays_shouldAddDays() {
        LocalDate date = LocalDate.of(2026, 9, 9);
        assertEquals(LocalDate.of(2026, 9, 19), DateUtils.plusDays(date, 10));
    }

    @Test
    void minusDays_shouldSubtractDays() {
        LocalDate date = LocalDate.of(2026, 9, 9);
        assertEquals(LocalDate.of(2026, 8, 30), DateUtils.minusDays(date, 10));
    }

    @Test
    void daysBetween_shouldCalculateCorrectly() {
        long days = DateUtils.daysBetween(LocalDate.of(2026, 9, 1), LocalDate.of(2026, 9, 9));
        assertEquals(8, days);
    }

    @Test
    void isLeapYear_byYear() {
        assertTrue(DateUtils.isLeapYear(2024));
        assertFalse(DateUtils.isLeapYear(2023));
        assertTrue(DateUtils.isLeapYear(2000));
        assertFalse(DateUtils.isLeapYear(1900));
    }

    @Test
    void isLeapYear_byLocalDate() {
        assertTrue(DateUtils.isLeapYear(LocalDate.of(2024, 1, 1)));
        assertFalse(DateUtils.isLeapYear(LocalDate.of(2023, 6, 15)));
    }

    @Test
    void isBetween_shouldReturnTrueWhenInRange() {
        LocalDate date = LocalDate.of(2026, 9, 5);
        assertTrue(DateUtils.isBetween(date, LocalDate.of(2026, 9, 1), LocalDate.of(2026, 9, 30)));
    }

    @Test
    void isBetween_shouldReturnFalseWhenOutOfRange() {
        LocalDate date = LocalDate.of(2026, 10, 1);
        assertFalse(DateUtils.isBetween(date, LocalDate.of(2026, 9, 1), LocalDate.of(2026, 9, 30)));
    }

    @Test
    void daysInMonth_shouldReturnCorrectDays() {
        assertEquals(30, DateUtils.daysInMonth(LocalDate.of(2026, 9, 1)));
        assertEquals(31, DateUtils.daysInMonth(LocalDate.of(2026, 1, 1)));
        assertEquals(28, DateUtils.daysInMonth(LocalDate.of(2023, 2, 1)));
        assertEquals(29, DateUtils.daysInMonth(LocalDate.of(2024, 2, 1)));
    }

    @Test
    void daysInYear_shouldReturnCorrectDays() {
        assertEquals(365, DateUtils.daysInYear(LocalDate.of(2023, 1, 1)));
        assertEquals(366, DateUtils.daysInYear(LocalDate.of(2024, 1, 1)));
    }

    @Test
    void toLocalDate_shouldConvertFromLegacyDate() {
        Date legacyDate = Date.from(LocalDate.of(2026, 9, 9).atStartOfDay(java.time.ZoneId.systemDefault()).toInstant());
        LocalDate result = DateUtils.toLocalDate(legacyDate);
        assertEquals(LocalDate.of(2026, 9, 9), result);
    }

    @Test
    void toDate_shouldConvertFromLocalDate() {
        Date result = DateUtils.toDate(LocalDate.of(2026, 9, 9));
        assertNotNull(result);
        assertEquals(LocalDate.of(2026, 9, 9), DateUtils.toLocalDate(result));
    }

    @Test
    void formatDateLegacy_shouldFormatCorrectly() {
        Date date = DateUtils.toDate(LocalDate.of(2026, 9, 9));
        String result = DateUtils.formatDate(date, "yyyy/MM/dd");
        assertEquals("2026/09/09", result);
    }

    @Test
    void parseDateLegacy_shouldParseCorrectly() throws ParseException {
        Date date = DateUtils.parseDateLegacy("2026-09-09", "yyyy-MM-dd");
        assertNotNull(date);
        assertEquals(LocalDate.of(2026, 9, 9), DateUtils.toLocalDate(date));
    }

    @Test
    void plusDaysLegacy_shouldAddDays() {
        Date date = DateUtils.toDate(LocalDate.of(2026, 9, 9));
        Date result = DateUtils.plusDaysLegacy(date, 5);
        assertEquals(LocalDate.of(2026, 9, 14), DateUtils.toLocalDate(result));
    }

    @Test
    void parseDate_shouldThrowOnNull() {
        assertThrows(NullPointerException.class, () -> DateUtils.parseDate(null));
    }
}