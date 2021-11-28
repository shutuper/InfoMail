package com.infopulse.infomail.dto.api;

import com.infopulse.infomail.dto.mail.EmailTemplateDTO;
import com.infopulse.infomail.dto.mail.RecipientDTO;
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
