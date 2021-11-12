package com.infopulse.infomail.services.scheduler.cronGenerator;

import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;

import static org.junit.jupiter.api.Assertions.*;

class CronExpressionBuilderTest {
    @Test
    void whenBuilder_butDayOfWeekAlreadySet_thenRuntimeException() {
        int value = DayOfWeek.SATURDAY.getValue();
        String actualMessage = assertThrows(RuntimeException.class,
                ()-> CronExpressionBuilder.builder()
                        .setDayOfWeek(Integer.toString(value))
                        .setDayOfMonth("1")
        ).getMessage();

        String expMessage = "We can't set DayOfMonth because DayOfWeek already set";
        assertEquals(expMessage, actualMessage);
    }

    @Test
    void whenBuilder_butDayOfMonthAlreadySet_thenRuntimeException() {
        int value = DayOfWeek.SATURDAY.getValue();
        String actualMessage = assertThrows(RuntimeException.class,
                ()-> CronExpressionBuilder.builder()
                        .setDayOfMonth("1")
                        .setDayOfWeek(Integer.toString(value))
        ).getMessage();

        String expMessage = "We can't set DayOfWeek because DayOfMonth already set";
        assertEquals(expMessage, actualMessage);
    }
}