package com.infopulse.infomail.dto.mail;

import com.infopulse.infomail.models.Template;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
