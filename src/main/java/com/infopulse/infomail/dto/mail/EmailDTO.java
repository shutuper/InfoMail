package com.infopulse.infomail.dto.mail;


import lombok.Data;

import java.util.List;

@Data
public class EmailDTO  {
	private List<RecipientDTO> recipients;
	private EmailTemplateDTO emailTemplate;
	private EmailScheduleDTO emailSchedule;

	public EmailDTO() {
	}
}
