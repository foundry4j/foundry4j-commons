package io.github.foundry4j.commons.date;

import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CalendarUtilsTest {

    private Date toDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, day, 12, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    @Test
    void calendar_shouldReturnNonNull() {
        assertNotNull(CalendarUtils.calendar());
    }

    @Test
    void getDayOfWeek_shouldReturnValidRange() {
        int dayOfWeek = CalendarUtils.getDayOfWeek();
        assertTrue(dayOfWeek >= Calendar.SUNDAY && dayOfWeek <= Calendar.SATURDAY);
    }

    @Test
    void getDayOfWeek_withDate() {
        // 2026-09-09 是星期三
        Date date = toDate(2026, 9, 9);
        assertEquals(Calendar.WEDNESDAY, CalendarUtils.getDayOfWeek(date));
    }

    @Test
    void isWeekend_saturday() {
        // 2026-09-12 是周六
        Date saturday = toDate(2026, 9, 12);
        assertTrue(CalendarUtils.isWeekend(saturday));
    }

    @Test
    void isWeekend_sunday() {
        // 2026-09-13 是周日
        Date sunday = toDate(2026, 9, 13);
        assertTrue(CalendarUtils.isWeekend(sunday));
    }

    @Test
    void isWeekend_weekday() {
        // 2026-09-09 是周三
        Date wednesday = toDate(2026, 9, 9);
        assertFalse(CalendarUtils.isWeekend(wednesday));
    }

    @Test
    void getFirstDayOfMonth_shouldReturnFirstDay() {
        Date date = toDate(2026, 9, 15);
        Date firstDay = CalendarUtils.getFirstDayOfMonth(date);

        Calendar cal = Calendar.getInstance();
        cal.setTime(firstDay);
        assertEquals(1, cal.get(Calendar.DAY_OF_MONTH));
        assertEquals(Calendar.SEPTEMBER, cal.get(Calendar.MONTH));
    }

    @Test
    void getLastDayOfMonth_shouldReturnLastDay() {
        Date date = toDate(2026, 9, 15);
        Date lastDay = CalendarUtils.getLastDayOfMonth(date);

        Calendar cal = Calendar.getInstance();
        cal.setTime(lastDay);
        assertEquals(30, cal.get(Calendar.DAY_OF_MONTH));
    }

    @Test
    void getFirstDayOfQuarter_q3() {
        // 9月属于第三季度，起始月为7月
        Date date = toDate(2026, 9, 15);
        Date firstDay = CalendarUtils.getFirstDayOfQuarter(date);

        Calendar cal = Calendar.getInstance();
        cal.setTime(firstDay);
        assertEquals(1, cal.get(Calendar.DAY_OF_MONTH));
        assertEquals(Calendar.JULY, cal.get(Calendar.MONTH));
    }

    @Test
    void getQuarter_shouldReturnCorrectQuarter() {
        assertEquals(1, CalendarUtils.getQuarter(toDate(2026, 2, 10)));
        assertEquals(2, CalendarUtils.getQuarter(toDate(2026, 5, 10)));
        assertEquals(3, CalendarUtils.getQuarter(toDate(2026, 9, 10)));
        assertEquals(4, CalendarUtils.getQuarter(toDate(2026, 12, 10)));
    }

    @Test
    void getFirstDayOfYear_shouldReturnJanFirst() {
        Date date = toDate(2026, 9, 15);
        Date firstDay = CalendarUtils.getFirstDayOfYear(date);

        Calendar cal = Calendar.getInstance();
        cal.setTime(firstDay);
        assertEquals(2026, cal.get(Calendar.YEAR));
        assertEquals(Calendar.JANUARY, cal.get(Calendar.MONTH));
        assertEquals(1, cal.get(Calendar.DAY_OF_MONTH));
    }

    @Test
    void getLastDayOfYear_shouldReturnDec31() {
        Date date = toDate(2026, 9, 15);
        Date lastDay = CalendarUtils.getLastDayOfYear(date);

        Calendar cal = Calendar.getInstance();
        cal.setTime(lastDay);
        assertEquals(Calendar.DECEMBER, cal.get(Calendar.MONTH));
        assertEquals(31, cal.get(Calendar.DAY_OF_MONTH));
    }

    @Test
    void getDaysInMonth_shouldReturnCorrectDays() {
        assertEquals(31, CalendarUtils.getDaysInMonth(2026, 1));
        assertEquals(28, CalendarUtils.getDaysInMonth(2026, 2));
        assertEquals(29, CalendarUtils.getDaysInMonth(2024, 2));
        assertEquals(30, CalendarUtils.getDaysInMonth(2026, 9));
    }

    @Test
    void getWeekOfYear_shouldReturnValidRange() {
        int week = CalendarUtils.getWeekOfYear(toDate(2026, 9, 9));
        assertTrue(week >= 1 && week <= 53);
    }

    @Test
    void getWeekOfYear_shouldBeConsistentForSameWeek() {
        int wed = CalendarUtils.getWeekOfYear(toDate(2026, 9, 9));
        int fri = CalendarUtils.getWeekOfYear(toDate(2026, 9, 11));
        assertEquals(wed, fri);
    }

    @Test
    void getDayOfWeek_shouldThrowOnNull() {
        assertThrows(NullPointerException.class, () -> CalendarUtils.getDayOfWeek((Date) null));
    }

    @Test
    void getFirstDayOfMonth_shouldThrowOnNull() {
        assertThrows(NullPointerException.class, () -> CalendarUtils.getFirstDayOfMonth(null));
    }
}