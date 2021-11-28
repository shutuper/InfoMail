package com.infopulse.infomail.dto.mail;

import com.infopulse.infomail.models.mail.EmailTemplate;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailTemplateDTO {

	private Long id;

	@NotBlank
	private String subject;
	@NotBlank
	private String body;

	public EmailTemplateDTO(EmailTemplate template) {
		this.subject = template.getSubject();
		this.body = template.getBody();
	}
}
