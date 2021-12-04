package com.infopulse.infomail.dto.api.templates;

import com.infopulse.infomail.models.templates.Template;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
