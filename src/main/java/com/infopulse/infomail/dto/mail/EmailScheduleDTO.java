package com.infopulse.infomail.dto.mail;

import com.infopulse.infomail.models.mail.enums.RepeatType;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailScheduleDTO {

	private boolean sendNow;

	private Timestamp sendDateTime;
	private RepeatType repeatAt;
	private Timestamp endDate;

	private List<Integer> daysOfWeek;
	private int dayOfMonth;        //1-31
	private int dayOfWeek;        //1-7
	private int numberOfWeek;    //1-4
	private int month;          //1-12

}
