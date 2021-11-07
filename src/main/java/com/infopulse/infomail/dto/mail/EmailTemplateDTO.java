package com.infopulse.infomail.dto.mail;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class EmailTemplateDTO {

	@NotBlank
	private String subject;
	@NotBlank
	private String body;

	public EmailTemplateDTO() {
	}
}