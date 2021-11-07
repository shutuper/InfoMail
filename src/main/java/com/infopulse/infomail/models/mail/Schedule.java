package com.infopulse.infomail.models.mail;

import com.infopulse.infomail.models.mail.enums.RepeatType;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface Schedule {

	boolean isSendNow();

	LocalDateTime getSendDateTime();

	LocalDate getEndDate();

	RepeatType getRepeatAt();

}
