package io.github.tuyucheng777.commons.date;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/// 日历操作工具类，基于旧式 Calendar API。
public final class CalendarUtils {

    private CalendarUtils() {
    }

    /// 获取当前 Calendar 实例。
    public static Calendar calendar() {
        return Calendar.getInstance();
    }

    /// 获取当前是星期几（1=周日, 2=周一, ..., 7=周六）。
    public static int getDayOfWeek() {
        return Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
    }

    /// 获取指定 Date 是星期几。
    public static int getDayOfWeek(Date date) {
        Objects.requireNonNull(date, "date must not be null");
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    /// 判断是否为周末（周六或周日）。
    public static boolean isWeekend(Date date) {
        int dayOfWeek = getDayOfWeek(date);
        return dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY;
    }

    /// 获取指定日期所在月份的第一天。
    public static Date getFirstDayOfMonth(Date date) {
        Objects.requireNonNull(date, "date must not be null");
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /// 获取指定日期所在月份的最后一天。
    public static Date getLastDayOfMonth(Date date) {
        Objects.requireNonNull(date, "date must not be null");
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }

    /// 获取指定日期所在季度的第一天。
    public static Date getFirstDayOfQuarter(Date date) {
        Objects.requireNonNull(date, "date must not be null");
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH);
        int quarterStartMonth;

        if (month < Calendar.APRIL) {
            quarterStartMonth = Calendar.JANUARY;
        } else if (month < Calendar.JULY) {
            quarterStartMonth = Calendar.APRIL;
        } else if (month < Calendar.OCTOBER) {
            quarterStartMonth = Calendar.JULY;
        } else {
            quarterStartMonth = Calendar.OCTOBER;
        }

        cal.set(Calendar.MONTH, quarterStartMonth);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /// 获取当前季度编号（1-4）。
    public static int getQuarter(Date date) {
        Objects.requireNonNull(date, "date must not be null");
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH) / 3 + 1;
    }

    /// 获取指定日期所在年份的第一天。
    public static Date getFirstDayOfYear(Date date) {
        Objects.requireNonNull(date, "date must not be null");
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /// 获取指定日期所在年份的最后一天。
    public static Date getLastDayOfYear(Date date) {
        Objects.requireNonNull(date, "date must not be null");
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.MONTH, Calendar.DECEMBER);
        cal.set(Calendar.DAY_OF_MONTH, 31);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }

    /// 获取指定月份的天数。
    public static int getDaysInMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /// 获取当前是第几周（基于 ISO 标准）。
    public static int getWeekOfYear(Date date) {
        Objects.requireNonNull(date, "date must not be null");
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.setMinimalDaysInFirstWeek(4);
        cal.setTime(date);
        return cal.get(Calendar.WEEK_OF_YEAR);
    }
}