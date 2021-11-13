package com.infopulse.infomail.dto.mail;


import com.infopulse.infomail.models.mail.EmailSchedule;
import lombok.Data;

import java.util.List;

@Data
public class EmailDTO  {
	private List<RecipientDTO> recipients;
	private EmailTemplateDTO emailTemplate;
	private EmailSchedule emailSchedule;

	public EmailDTO() {
	}
}
