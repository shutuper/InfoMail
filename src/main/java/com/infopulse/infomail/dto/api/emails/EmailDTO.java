package com.infopulse.infomail.dto.api.emails;


import com.infopulse.infomail.dto.api.schedule.EmailScheduleDTO;
import com.infopulse.infomail.dto.api.templates.EmailTemplateDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailDTO {

	private List<RecipientDTO> recipients;

	@Valid
	@NotNull
	private EmailTemplateDTO emailTemplate;

	@Valid
	@NotNull
	private EmailScheduleDTO emailSchedule;

}
