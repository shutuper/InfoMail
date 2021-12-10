package com.infopulse.infomail.models.schedule;

import com.infopulse.infomail.dto.api.schedule.EmailScheduleDTO;
import com.infopulse.infomail.exceptions.EmailScheduleException;
import com.infopulse.infomail.models.mail.enums.RepeatType;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
public class EmailSchedule implements Schedule {

	private boolean sendNow;

	private LocalDateTime sendDateTime;
	private RepeatType repeatAt;
	private LocalDate endDate;

	@Builder.Default
	private List<Integer> daysOfWeek = new ArrayList<>();
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
		try {
			EmailScheduleBuilder builder =
					EmailSchedule
							.builder()
							.sendNow(dto.isSendNow())
							.daysOfWeek(dto.getDaysOfWeek())
							.dayOfMonth(dto.getDayOfMonth())
							.dayOfWeek(dto.getDayOfWeek())
							.numberOfWeek(dto.getNumberOfWeek())
							.month(dto.getMonth());

			RepeatType repeatAt = dto.getRepeatAt();
			if (Objects.isNull(repeatAt))
				return builder
						.repeatAt(RepeatType.NOTHING)
						.build();

			Timestamp sendDateTime = dto.getSendDateTime();
			Timestamp endDate = dto.getEndDate();

			if (repeatAt.equals(RepeatType.NOTHING)) {
				checkSendDateTime(sendDateTime, builder);
				builder
						.repeatAt(repeatAt);
			} else {
				builder.repeatAt(repeatAt);
				checkSendDateTime(sendDateTime, builder);
				checkEndDate(endDate, builder);
			}
			return builder.build();
		} catch (NullPointerException ex) {
			log.error(ex.getMessage(), ex);
			throw new EmailScheduleException(ex.getMessage());
		}
	}

	private static void checkEndDate(Timestamp endDate, EmailScheduleBuilder builder) {
		if (Objects.nonNull(endDate)) {
			builder.endDate(endDate.toLocalDateTime().toLocalDate());
		} else throw new NullPointerException("End date is missing");
	}

	private static void checkSendDateTime(Timestamp sendDateTime, EmailScheduleBuilder builder) {
		if(Objects.nonNull(sendDateTime)) {
			builder.sendDateTime(sendDateTime.toLocalDateTime());
		} else throw new NullPointerException("Send date time is missing");
	}
}
