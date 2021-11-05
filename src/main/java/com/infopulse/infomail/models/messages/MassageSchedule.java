package com.infopulse.infomail.models.messages;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class MassageSchedule {

    private LocalDateTime sendDate;
    private RepeatType repeatAt;
    private LocalDate endDate;

    private List<Integer> daysOfWeek;
    private int dayOfMonth;					//1-31
    private int dayOfWeek;					//1-7
    private int numberOfWeek;				//1-4
    private int month;                      //1-12

    public MassageSchedule() {
    }

    public LocalDateTime getSendDate() {
        return sendDate;
    }

    public void setSendDate(LocalDateTime sendDate) {
        this.sendDate = sendDate;
    }

    public RepeatType getRepeatAt() {
        return repeatAt;
    }

    public void setRepeatAt(RepeatType repeatAt) {
        this.repeatAt = repeatAt;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public List<Integer> getDaysOfWeek() {
        return daysOfWeek;
    }

    public void setDaysOfWeek(List<Integer> daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public int getNumberOfWeek() {
        return numberOfWeek;
    }

    public void setNumberOfWeek(int numberOfWeek) {
        this.numberOfWeek = numberOfWeek;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }
}
