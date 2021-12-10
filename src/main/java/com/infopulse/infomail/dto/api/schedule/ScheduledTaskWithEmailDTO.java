package com.infopulse.infomail.dto.api.schedule;


import com.infopulse.infomail.dto.app.ScheduledTaskRaw;
import com.infopulse.infomail.dto.api.emails.RecipientDTO;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;


@Slf4j
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ScheduledTaskWithEmailDTO extends ScheduledTaskDTO {

	private String subject;
	private String body;
	private List<RecipientDTO> recipients;

	public ScheduledTaskWithEmailDTO(ScheduledTaskRaw scheduledTask, List<RecipientDTO> recipients) {
		super(scheduledTask);
		this.subject = scheduledTask.getSubject();
		this.body = scheduledTask.getBody();
		this.recipients = recipients;
	}

}
