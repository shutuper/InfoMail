package com.infopulse.infomail.services.scheduler.cronGenerator;

import com.infopulse.infomail.models.emails.EmailSchedule;

import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.Month;
import java.util.List;

public class CronGenerator {
    /**
     * Generate a CRON expression is a string comprising 6 or 7 fields separated by white space.
     *
     * @param seconds    mandatory = yes. allowed values = {@code  0-59    * / , -}
     * @param minutes    mandatory = yes. allowed values = {@code  0-59    * / , -}
     * @param hours      mandatory = yes. allowed values = {@code 0-23   * / , -}
     * @param dayOfMonth mandatory = yes. allowed values = {@code 1-31  * / , - ? L W}
     * @param month      mandatory = yes. allowed values = {@code 1-12 or JAN-DEC    * / , -}
     * @param dayOfWeek  mandatory = yes. allowed values = {@code 0-6 or SUN-SAT * / , - ? L #}
     * @param year       mandatory = no. allowed values = {@code 1970–2099    * / , -}
     * @return a CRON Formatted String.
     */
    public static String generate(final String seconds, final String minutes, final String hours, final String dayOfMonth, final String month, final String dayOfWeek, final String year)
    {
        return String.format("%1$s %2$s %3$s %4$s %5$s %6$s %7$s", seconds, minutes, hours, dayOfMonth, month, dayOfWeek, year);
    }

    public static String generate(EmailSchedule schedule) {

        switch (schedule.getRepeatAt()) {
            case EVERY_DAY: return whenEveryDay(schedule);
            case EVERY_WEEK: return whenEveryWeek(schedule);
            case EVERY_MONTH: return whenEveryMonth(schedule);
            case EVERY_YEAR: return whenEveryYear(schedule);

            default: throw new IllegalArgumentException(
                    "Not define methods for parse schedule with RepeatType: " + schedule.getRepeatAt());
        }
    }

    static String whenEveryDay(EmailSchedule schedule) {
        return CronExpression.builder()
                .setHours(schedule.getSendDate().getHour())
                .setMinutes(schedule.getSendDate().getMinute())
                .build()
                    .toString();
    }

    static String whenEveryWeek(EmailSchedule schedule) {
        String daysOfWeek = parseDaysOfWeek(schedule);

        return CronExpression.builder()
                .setHours(schedule.getSendDate().getHour())
                .setMinutes(schedule.getSendDate().getMinute())
                .setDayOfWeek(daysOfWeek)
                .build()
                .toString();
    }

    static String whenEveryMonth(EmailSchedule schedule) {
        CronExpression.Builder builder = CronExpression.builder()
                .setHours(schedule.getSendDate().getHour())
                .setMinutes(schedule.getSendDate().getMinute());

        String dayOfWeek = parseDayOfWeekAndNumberOfWeek(schedule);
        if(! dayOfWeek.equals("?")) {
            return builder
                    .setDayOfWeek(dayOfWeek)
                    .build().toString();
        }

        String dayOfMonth = parseDayOfMonth(schedule);
        return builder
                .setDayOfMonth(dayOfMonth)
                .build().toString();
    }

    static String whenEveryYear(EmailSchedule schedule) {
        CronExpression.Builder builder = CronExpression.builder()
                .setHours(schedule.getSendDate().getHour())
                .setMinutes(schedule.getSendDate().getMinute());

        String month = parseMonth(schedule);
        builder.setMonth(month);

        String dayOfWeek = parseDayOfWeekAndNumberOfWeek(schedule);
        if(! dayOfWeek.equals("?")) {
            return builder
                    .setDayOfWeek(dayOfWeek)
                    .build().toString();
        }

        String dayOfMonth = parseDayOfMonth(schedule);
        return builder
                .setDayOfMonth(dayOfMonth)
                .build().toString();
    }

    static String parseDaysOfWeek(EmailSchedule schedule) {
        List<Integer> daysOfWeek = schedule.getDaysOfWeek();
        if(daysOfWeek == null || daysOfWeek.isEmpty()) {
            int dayOfWeekValue = schedule.getSendDate().getDayOfWeek().getValue();
            dayOfWeekValue = dayOfWeekConvertor(dayOfWeekValue);
            return Integer.toString(dayOfWeekValue);
        }

        StringBuilder builder= new StringBuilder();
        daysOfWeek.forEach(dayOfWeekValue -> {
            DayOfWeek.of(dayOfWeekValue); //check in DayOfWeek
            builder.append(dayOfWeekValue).append(',');
        });
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }

    static String parseDayOfMonth(EmailSchedule schedule) {
        int dayOfMonth = schedule.getDayOfMonth();
        if(dayOfMonth != 0) {
            checkDayOfMonthValue(dayOfMonth);
            return String.valueOf(dayOfMonth);
        }
        return String.valueOf(schedule.getSendDate().getDayOfMonth());
    }

    static String parseDayOfWeekAndNumberOfWeek(EmailSchedule schedule) {
        int dayOfWeekValue = schedule.getDayOfWeek();
        int numberOfWeek = schedule.getNumberOfWeek();
        if (dayOfWeekValue != 0 && numberOfWeek != 0) {
            DayOfWeek.of(dayOfWeekValue);    //check in DayOfWeek
            checkNumberOfWeekValue(numberOfWeek);

            dayOfWeekValue = dayOfWeekConvertor(dayOfWeekValue);
            return String.format("%1$s#%2$s", dayOfWeekValue, numberOfWeek);
        } else {
            return "?";
        }
    }

    static String parseMonth(EmailSchedule schedule) {
        int monthValue = schedule.getMonth();
        if(monthValue != 0) {
            Month.of(monthValue);   //check monthValue in Month
            return Integer.toString(monthValue);
        }

        return String.valueOf(schedule.getSendDate().getMonth().getValue());
    }

    private static void checkNumberOfWeekValue(int numberOfWeek) {
        if((numberOfWeek > 4) || (numberOfWeek < 1)) throw new DateTimeException("Invalid value for NumberOfWeek: " + numberOfWeek);
    }

    private static void checkDayOfMonthValue(int dayOfMonth) {
        if((dayOfMonth > 31) || (dayOfMonth < 1)) throw new DateTimeException("Invalid value for DayOfMonth: " + dayOfMonth);
    }

    /**
     * In Cron Weeks starter at Sunday and finish at Saturday
     * So Sunday index 0, Saturday index 6
     * But in {@code DayOfWeek} week start at Monday
     */
    private static int dayOfWeekConvertor(int dayOfWeekValue) {
        dayOfWeekValue++;
        if(dayOfWeekValue == 8) dayOfWeekValue = 1;
        return dayOfWeekValue;
    }

}
