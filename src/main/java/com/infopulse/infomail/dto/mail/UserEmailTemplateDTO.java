package com.infopulse.infomail.dto.mail;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
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

}
