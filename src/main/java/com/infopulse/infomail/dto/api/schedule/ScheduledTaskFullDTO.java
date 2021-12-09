package com.infopulse.infomail.dto.api.schedule;


import com.infopulse.infomail.dto.app.ScheduledTaskFullRaw;
import com.infopulse.infomail.dto.api.emails.RecipientDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class ScheduledTaskFullDTO {

	private Long orderId;
	private String jobName;
	private String description;
	private Timestamp startAt;
	private Timestamp endAt;
	private String state;

	private String subject;
	private String body;

	List<RecipientDTO> recipients;

	public ScheduledTaskFullDTO(ScheduledTaskFullRaw scheduledTask, List<RecipientDTO> recipients) {
		this.orderId = scheduledTask.getOrderId();
		this.jobName = scheduledTask.getJobName();
		this.description = scheduledTask.getDescription();
		this.subject = scheduledTask.getSubject();
		this.body = scheduledTask.getBody();

		String stateTemp = scheduledTask.getTriggerState();
		boolean isTriggerExpired = Objects.isNull(stateTemp);

		setTriggerInfo(scheduledTask, stateTemp, isTriggerExpired);

		this.recipients = recipients;
	}

	private void setTriggerInfo(ScheduledTaskFullRaw scheduledTask, String stateTemp, boolean triggerIsExpired) {
		if (triggerIsExpired)
			this.state = "FINISHED";
		else {
			Long startAtTemp = scheduledTask.getStartAt();
			Long endAtTemp = scheduledTask.getEndAt();
			this.state = stateTemp;

			this.startAt = Objects.isNull(startAtTemp) ?
					null : Timestamp.from(Instant.ofEpochMilli(startAtTemp));
			this.endAt = Objects.isNull(endAtTemp) ?
					null : Timestamp.from(Instant.ofEpochMilli(endAtTemp));
		}
	}
}
