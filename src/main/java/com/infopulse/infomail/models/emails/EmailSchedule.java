package com.infopulse.infomail.models.emails;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class EmailSchedule {

    private boolean sendNow;
    private LocalDateTime sendDate;
    private RepeatType repeatAt;
    private LocalDate endDate;

    private List<Integer> daysOfWeek;
    private int dayOfMonth;					//1-31
    private int dayOfWeek;					//1-7
    private int numberOfWeek;				//1-4
    private int month;                      //1-12

    public EmailSchedule() {
    }
}
