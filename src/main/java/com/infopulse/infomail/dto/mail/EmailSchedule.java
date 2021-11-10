package com.infopulse.infomail.dto.mail;

import com.infopulse.infomail.models.mail.Schedule;
import com.infopulse.infomail.models.mail.enums.RepeatType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
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
