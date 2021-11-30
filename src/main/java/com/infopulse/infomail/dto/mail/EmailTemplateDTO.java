package com.infopulse.infomail.dto.mail;

import com.infopulse.infomail.models.Template;
import com.infopulse.infomail.models.mail.EmailTemplate;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailTemplateDTO {

	private Long id;

	private String subject;

	private String body;

	public EmailTemplateDTO(Template template) {
		this.subject = template.getSubject();
		this.body = template.getBody();
	}

}
