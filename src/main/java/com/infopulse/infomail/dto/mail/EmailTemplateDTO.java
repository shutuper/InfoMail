package com.infopulse.infomail.dto.mail;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
public class EmailTemplateDTO {

	private Long id;

	@NotBlank
	private String subject;
	@NotBlank
	private String body;

	public EmailTemplateDTO() {
	}
}
