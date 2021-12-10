package com.infopulse.infomail.dto.api.templates;

import com.infopulse.infomail.models.templates.UserEmailTemplate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

	public UserEmailTemplateDTO(UserEmailTemplate template) {
		this(
			template.getId(),
			template.getName(),
			template.getSubject(),
			template.getBody(),
			template.getUserEmail(),
			template.getSharingLink());
	}
}
