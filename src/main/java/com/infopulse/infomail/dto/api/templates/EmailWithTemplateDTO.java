package com.infopulse.infomail.dto.api.templates;

import com.infopulse.infomail.dto.api.emails.ExecutedEmailDTO;
import com.infopulse.infomail.dto.api.emails.RecipientDTO;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailWithTemplateDTO {

	ExecutedEmailDTO email;
	EmailTemplateDTO template;
	List<RecipientDTO> recipients;

}
