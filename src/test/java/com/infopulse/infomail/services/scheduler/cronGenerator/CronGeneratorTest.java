package com.infopulse.infomail.services.scheduler.cronGenerator;

import com.infopulse.infomail.models.mail.EmailSchedule;
import com.infopulse.infomail.models.mail.enums.RepeatType;
import org.junit.jupiter.api.Test;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CronGeneratorTest {

    @Test
    void generate_thenString() {
        final String seconds = "0";
        final String minutes = "0";
        final String hours = "2";
        final String dayOfMonth = "3";
        final String month = "NOV";
        final String dayOfWeek = "?";
        final String year = "2021";

        String exp = "0 0 2 3 NOV ? 2021";

        String actual = CronGenerator.generate(
                seconds,
                minutes,
                hours,
                dayOfMonth,
                month,
                dayOfWeek,
                year);

        assertEquals(exp, actual);
    }

    @Test
    void generate_thenIllegalArgumentException() {
        EmailSchedule schedule = new EmailSchedule();
        schedule.setRepeatAt(RepeatType.OTHER);

        String actualMessage = assertThrows(IllegalArgumentException.class,
                () -> CronGenerator.generate(schedule)).getMessage();
        String expMessage = "Not define methods for parse messageSchedule with RepeatType = OTHER";
        assertEquals(expMessage, actualMessage);
    }

    @Test
    void whenEveryDay() {
        LocalDateTime sendDate = LocalDateTime.of(2021, 11, 5, 12, 0);
        EmailSchedule schedule = new EmailSchedule();
        schedule.setSendDateTime(sendDate);
        schedule.setRepeatAt(RepeatType.EVERY_DAY);

        String cron = CronGenerator.whenEveryDay(schedule);

        String exp = "0 0 12 ? * * *";
        assertEquals(exp, cron);

        cron = CronGenerator.generate(schedule);
        assertEquals(exp, cron);
    }


    @Test
    void whenEveryWeek() {
        LocalDateTime sendDate = LocalDateTime.of(2021, 11, 5, 12, 0);
        EmailSchedule schedule = new EmailSchedule();
        schedule.setSendDateTime(sendDate);
        schedule.setRepeatAt(RepeatType.EVERY_WEEK);

        String exp = "0 0 12 ? * 6 *";

        String cron = CronGenerator.whenEveryWeek(schedule);
        assertEquals(exp, cron);

        cron = CronGenerator.generate(schedule);
        assertEquals(exp, cron);
    }

    @Test
    void parseDaysOfWeek() {
        LocalDateTime sendDate = LocalDateTime.of(2021, 11, 5, 12, 0);
        List<Integer> daysOfWeek = Arrays.asList(1,2,3,4);

        EmailSchedule schedule = new EmailSchedule();
        schedule.setSendDateTime(sendDate);
        schedule.setRepeatAt(RepeatType.EVERY_WEEK);
        schedule.setDaysOfWeek(daysOfWeek);

        String exp = "1,2,3,4";

        String cron = CronGenerator.parseDaysOfWeek(schedule);
        assertEquals(exp, cron);
    }

    @Test
    void parseDaysOfWeek_butDayValueIllegal_thenDateTimeException() {
        LocalDateTime sendDate = LocalDateTime.of(2021, 11, 5, 12, 0);
        List<Integer> daysOfWeek = Arrays.asList(1,12,3,4);

        EmailSchedule schedule = new EmailSchedule();
        schedule.setSendDateTime(sendDate);
        schedule.setRepeatAt(RepeatType.EVERY_WEEK);
        schedule.setDaysOfWeek(daysOfWeek);

        String actualMessage = assertThrows(DateTimeException.class,
                () -> CronGenerator.generate(schedule)).getMessage();
        String expMessage = "Invalid value for DayOfWeek: 12";
        assertEquals(expMessage, actualMessage);
    }

    @Test
    void whenEveryMonth() {
        LocalDateTime sendDate = LocalDateTime.of(2021, 11, 5, 12, 0);
        EmailSchedule schedule = new EmailSchedule();
        schedule.setSendDateTime(sendDate);
        schedule.setRepeatAt(RepeatType.EVERY_MONTH);

        String exp = "0 0 12 5 * ? *";

        String cron = CronGenerator.whenEveryMonth(schedule);
        assertEquals(exp, cron);

        cron = CronGenerator.generate(schedule);
        assertEquals(exp, cron);
    }

    @Test
    void whenEveryMonth_butGetDayOfMonthNotNull() {
        LocalDateTime sendDate = LocalDateTime.of(2021, 11, 5, 12, 0);
        EmailSchedule schedule = new EmailSchedule();
        schedule.setSendDateTime(sendDate);
        schedule.setRepeatAt(RepeatType.EVERY_MONTH);
        schedule.setDayOfMonth(15);

        String exp = "0 0 12 15 * ? *";

        String cron = CronGenerator.whenEveryMonth(schedule);
        assertEquals(cron, exp);

        cron = CronGenerator.generate(schedule);
        assertEquals(cron, exp);
    }

    @Test
    void whenEveryMonth_butDayOfWeekAndNumberOfWeekNotNUll() {
        LocalDateTime sendDate = LocalDateTime.of(2021, 11, 5, 12, 0);
        EmailSchedule schedule = new EmailSchedule();
        schedule.setSendDateTime(sendDate);
        schedule.setRepeatAt(RepeatType.EVERY_MONTH);
        schedule.setDayOfWeek(1);
        schedule.setNumberOfWeek(2);

        String exp = "0 0 12 ? * 2#2 *";

        String cron = CronGenerator.whenEveryMonth(schedule);
        assertEquals(exp, cron);

        cron = CronGenerator.generate(schedule);
        assertEquals(exp, cron);
    }

    @Test
    void parseDayOfMonth_butGetDayOfMonthNotNull() {
        LocalDateTime sendDate = LocalDateTime.of(2021, 11, 5, 12, 0);
        EmailSchedule schedule = new EmailSchedule();
        schedule.setSendDateTime(sendDate);
        schedule.setRepeatAt(RepeatType.EVERY_MONTH);
        schedule.setDayOfMonth(15);

        String exp = "15";

        String dayOfMonth = CronGenerator.parseDayOfMonth(schedule);
        assertEquals(dayOfMonth, exp);
    }

    @Test
    void parseDayOfMonth_butDayOfMonthIllegal_thenDateTimeException() {
        LocalDateTime sendDate = LocalDateTime.of(2021, 11, 5, 12, 0);
        EmailSchedule schedule = new EmailSchedule();
        schedule.setSendDateTime(sendDate);
        schedule.setRepeatAt(RepeatType.EVERY_MONTH);
        schedule.setDayOfMonth(-5);

        String actualMessage = assertThrows(DateTimeException.class,
                () -> CronGenerator.parseDayOfMonth(schedule)).getMessage();
        String expMessage = "Invalid value for DayOfMonth: -5";
        assertEquals(expMessage, actualMessage);
    }

    @Test
    void parseDayOfMonth_butDayOfWeekAndNumberOfWeekNotNull() {
        LocalDateTime sendDate = LocalDateTime.of(2021, 11, 5, 12, 0);
        EmailSchedule schedule = new EmailSchedule();
        schedule.setSendDateTime(sendDate);
        schedule.setRepeatAt(RepeatType.EVERY_MONTH);
        schedule.setDayOfWeek(6);
        schedule.setNumberOfWeek(3);

        String exp = "7#3";

        String dayOfMonth = CronGenerator.parseDayOfWeekAndNumberOfWeek(schedule);
        assertEquals(exp, dayOfMonth);
    }

    @Test
    void parseDayOfMonth_butDayOfWeekIllegal_thenDateTimeException() {
        LocalDateTime sendDate = LocalDateTime.of(2021, 11, 5, 12, 0);
        EmailSchedule schedule = new EmailSchedule();
        schedule.setSendDateTime(sendDate);
        schedule.setRepeatAt(RepeatType.EVERY_MONTH);
        schedule.setDayOfWeek(66);
        schedule.setNumberOfWeek(3);

        String actualMessage = assertThrows(DateTimeException.class,
                () -> CronGenerator.parseDayOfWeekAndNumberOfWeek(schedule)).getMessage();
        String expMessage = "Invalid value for DayOfWeek: 66";
        assertEquals(expMessage, actualMessage);
    }

    @Test
    void parseDayOfMonth_butNumberOfWeekIllegal_thenDateTimeException() {
        LocalDateTime sendDate = LocalDateTime.of(2021, 11, 5, 12, 0);
        EmailSchedule schedule = new EmailSchedule();
        schedule.setSendDateTime(sendDate);
        schedule.setRepeatAt(RepeatType.EVERY_MONTH);
        schedule.setDayOfWeek(6);
        schedule.setNumberOfWeek(33);

        String actualMessage = assertThrows(DateTimeException.class,
                () -> CronGenerator.parseDayOfWeekAndNumberOfWeek(schedule)).getMessage();
        String expMessage = "Invalid value for NumberOfWeek: 33";
        assertEquals(expMessage, actualMessage);
    }

    @Test
    void whenEveryYear_butMonthNotNull() {
        LocalDateTime sendDate = LocalDateTime.of(2021, Month.FEBRUARY, 5, 12, 0);
        EmailSchedule schedule = new EmailSchedule();
        schedule.setSendDateTime(sendDate);
        schedule.setRepeatAt(RepeatType.EVERY_YEAR);
        schedule.setMonth(12);

        String exp = "0 0 12 5 12 ? *";

        String cron = CronGenerator.whenEveryYear(schedule);
        assertEquals(exp, cron);

        cron = CronGenerator.generate(schedule);
        assertEquals(exp, cron);
    }

    @Test
    void whenEveryYear_butDayOfMonthNotNUll() {
        LocalDateTime sendDate = LocalDateTime.of(2021, Month.FEBRUARY, 5, 12, 0);
        EmailSchedule schedule = new EmailSchedule();
        schedule.setSendDateTime(sendDate);
        schedule.setRepeatAt(RepeatType.EVERY_YEAR);
        schedule.setDayOfMonth(30);

        String exp = "0 0 12 30 2 ? *";

        String cron = CronGenerator.whenEveryYear(schedule);
        assertEquals(exp, cron);

        cron = CronGenerator.generate(schedule);
        assertEquals(exp, cron);
    }

    @Test
    void whenEveryYear_butDayOfWeekAndNumberOfWeekNotNUll() {
        LocalDateTime sendDate = LocalDateTime.of(2021, Month.FEBRUARY, 5, 12, 0);
        EmailSchedule schedule = new EmailSchedule();
        schedule.setSendDateTime(sendDate);
        schedule.setRepeatAt(RepeatType.EVERY_YEAR);
        schedule.setDayOfWeek(1);
        schedule.setNumberOfWeek(2);

        String exp = "0 0 12 ? 2 2#2 *";

        String cron = CronGenerator.whenEveryYear(schedule);
        assertEquals(exp, cron);

        cron = CronGenerator.generate(schedule);
        assertEquals(exp, cron);
    }

    @Test
    void parseMonth_butMonthValueIllegal_thenDateTimeException() {
        LocalDateTime sendDate = LocalDateTime.of(2021, Month.FEBRUARY, 5, 12, 0);
        EmailSchedule schedule = new EmailSchedule();
        schedule.setSendDateTime(sendDate);
        schedule.setRepeatAt(RepeatType.EVERY_YEAR);
        schedule.setMonth(33);

        String actualMessage = assertThrows(DateTimeException.class,
                () -> CronGenerator.parseMonth(schedule)).getMessage();
        String expMessage = "Invalid value for MonthOfYear: 33";
        assertEquals(expMessage, actualMessage);
    }
}
