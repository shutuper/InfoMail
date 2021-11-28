package com.infopulse.infomail.dto.mail;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailDTO {

	private List<RecipientDTO> recipients;
	private EmailTemplateDTO emailTemplate;
	private EmailScheduleDTO emailSchedule;

}
