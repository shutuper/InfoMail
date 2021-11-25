package com.infopulse.infomail.dto.mail;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
public class UserEmailTemplateDTO {

	private Long id;

	@NotBlank
	private String name;

	@NotBlank
	private String subject;

	@NotBlank
	private String body;

	private String userEmail;

	private String sharingLink;

	public UserEmailTemplateDTO() {
	}
}
