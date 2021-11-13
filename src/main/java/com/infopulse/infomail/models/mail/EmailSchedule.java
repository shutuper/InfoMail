package com.infopulse.infomail.models.mail;

import com.infopulse.infomail.models.mail.enums.RepeatType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class EmailSchedule implements Schedule {

	private boolean sendNow;

	private LocalDateTime sendDateTime;
	private RepeatType repeatAt;
	private LocalDate endDate;

	private List<Integer> daysOfWeek;
	private int dayOfMonth;        //1-31
	private int dayOfWeek;        //1-7
	private int numberOfWeek;    //1-4
	private int month;          //1-12

	public EmailSchedule() {
	}

}
