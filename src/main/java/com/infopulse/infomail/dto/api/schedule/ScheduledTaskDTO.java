package com.infopulse.infomail.dto.api.schedule;


import com.infopulse.infomail.dto.app.ScheduledTaskFullRaw;
import com.infopulse.infomail.dto.app.ScheduledTaskRaw;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Objects;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ScheduledTaskDTO {

	private Long orderId;
	private String jobName;
	private String description;
	private Timestamp startAt;
	private Timestamp endAt;
	private String state;
	private String subject;

	public ScheduledTaskDTO(ScheduledTaskFullRaw scheduledTaskRaw) {
		this.orderId = scheduledTaskRaw.getOrderId();
		this.jobName = scheduledTaskRaw.getJobName();
		this.description = scheduledTaskRaw.getDescription();
		this.subject = scheduledTaskRaw.getSubject();

		String stateTemp = scheduledTaskRaw.getTriggerState();
		boolean isTriggerExpired = Objects.isNull(stateTemp);

		System.out.println("\nStart at: " + scheduledTaskRaw.getStartAt() + ", end at" + scheduledTaskRaw.getEndAt());

		setTriggerInfo(scheduledTaskRaw, stateTemp, isTriggerExpired);

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
