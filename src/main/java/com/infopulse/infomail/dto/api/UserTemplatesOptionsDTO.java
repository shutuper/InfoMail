package com.infopulse.infomail.dto.api;

import com.infopulse.infomail.models.mail.UserEmailTemplate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTemplatesOptionsDTO {

	private Long id;
	private String name;


	public UserTemplatesOptionsDTO(UserEmailTemplate template) {
		this.id = template.getId();
		this.name = template.getName();
	}
}
