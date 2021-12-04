package com.infopulse.infomail.dto.api;


import com.infopulse.infomail.dto.app.ScheduledTaskFullRaw;
import com.infopulse.infomail.dto.mail.RecipientDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Data
@ToString
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
		boolean triggerIsExpired = Objects.isNull(stateTemp);

		System.out.println("\nStart at: " + scheduledTask.getStartAt() + ", end at" + scheduledTask.getEndAt());

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

		this.recipients = recipients;
	}
}
