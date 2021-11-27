package com.infopulse.infomail.models.mail;

import com.infopulse.infomail.dto.mail.EmailScheduleDTO;
import com.infopulse.infomail.models.mail.enums.RepeatType;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

	public EmailScheduleDTO toDTO() {
		EmailScheduleDTO dto = new EmailScheduleDTO();
		dto.setSendNow(this.sendNow);

		if (this.sendDateTime != null) dto.setSendDateTime(Timestamp.valueOf(this.sendDateTime));
		dto.setRepeatAt(this.repeatAt);
		if (this.endDate != null) dto.setEndDate(Timestamp.valueOf(LocalDateTime.of(this.endDate, LocalTime.now())));

		dto.setDaysOfWeek(this.daysOfWeek);
		dto.setDayOfMonth(this.dayOfMonth);
		dto.setDayOfWeek(this.dayOfWeek);
		dto.setNumberOfWeek(this.numberOfWeek);
		dto.setMonth(this.month);

		return dto;
	}

	public static EmailSchedule fromDTO(EmailScheduleDTO dto) {
		EmailSchedule schedule = new EmailSchedule();
		schedule.setSendNow(dto.isSendNow());

		if (dto.getSendDateTime() != null) schedule.setSendDateTime(dto.getSendDateTime().toLocalDateTime());
		schedule.setRepeatAt(dto.getRepeatAt());
		if (dto.getEndDate() != null) schedule.setEndDate(dto.getEndDate().toLocalDateTime().toLocalDate());

		if (dto.getDaysOfWeek() != null) schedule.setDaysOfWeek(dto.getDaysOfWeek());
		schedule.setDayOfMonth(dto.getDayOfMonth());
		schedule.setDayOfWeek(dto.getDayOfWeek());
		schedule.setNumberOfWeek(dto.getNumberOfWeek());
		schedule.setMonth(dto.getMonth());

		return schedule;
	}
}
