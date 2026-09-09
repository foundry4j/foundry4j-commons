package io.github.tuyucheng777.commons.date;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class TimeUtilsTest {

    @Test
    void now_shouldReturnCurrentTime() {
        LocalTime now = TimeUtils.now();
        assertNotNull(now);
    }

    @Test
    void parseTime_defaultPattern() {
        LocalTime time = TimeUtils.parseTime("14:30:00");
        assertEquals(LocalTime.of(14, 30, 0), time);
    }

    @Test
    void parseTime_customPattern() {
        LocalTime time = TimeUtils.parseTime("14:30", "HH:mm");
        assertEquals(LocalTime.of(14, 30, 0), time);
    }

    @Test
    void formatTime_defaultPattern() {
        String result = TimeUtils.formatTime(LocalTime.of(14, 30, 45));
        assertEquals("14:30:45", result);
    }

    @Test
    void formatTime_customPattern() {
        String result = TimeUtils.formatTime(LocalTime.of(14, 30, 45), "HH:mm");
        assertEquals("14:30", result);
    }

    @Test
    void secondsBetween_shouldCalculateCorrectly() {
        long seconds = TimeUtils.secondsBetween(LocalTime.of(10, 0, 0), LocalTime.of(10, 5, 30));
        assertEquals(330, seconds);
    }

    @Test
    void durationBetween_shouldReturnDuration() {
        Duration duration = TimeUtils.durationBetween(LocalTime.of(10, 0, 0), LocalTime.of(12, 30, 0));
        assertEquals(Duration.ofHours(2).plusMinutes(30), duration);
    }

    @Test
    void plusHours_shouldAddHours() {
        LocalTime time = TimeUtils.plusHours(LocalTime.of(10, 0, 0), 3);
        assertEquals(LocalTime.of(13, 0, 0), time);
    }

    @Test
    void plusMinutes_shouldAddMinutes() {
        LocalTime time = TimeUtils.plusMinutes(LocalTime.of(10, 0, 0), 45);
        assertEquals(LocalTime.of(10, 45, 0), time);
    }

    @Test
    void isBetween_shouldReturnTrueWhenInRange() {
        assertTrue(TimeUtils.isBetween(LocalTime.of(12, 0, 0), LocalTime.of(9, 0, 0), LocalTime.of(18, 0, 0)));
    }

    @Test
    void isBetween_shouldReturnFalseWhenOutOfRange() {
        assertFalse(TimeUtils.isBetween(LocalTime.of(20, 0, 0), LocalTime.of(9, 0, 0), LocalTime.of(18, 0, 0)));
    }

    @Test
    void isWorkTime_shouldReturnTrueDuringWorkHours() {
        assertTrue(TimeUtils.isWorkTime(LocalTime.of(10, 30, 0)));
    }

    @Test
    void isWorkTime_shouldReturnFalseOutsideWorkHours() {
        assertFalse(TimeUtils.isWorkTime(LocalTime.of(20, 0, 0)));
    }

    @Test
    void getHour_shouldReturnCorrectHour() {
        assertEquals(14, TimeUtils.getHour(LocalTime.of(14, 30, 0)));
    }

    @Test
    void getMinute_shouldReturnCorrectMinute() {
        assertEquals(30, TimeUtils.getMinute(LocalTime.of(14, 30, 0)));
    }
}