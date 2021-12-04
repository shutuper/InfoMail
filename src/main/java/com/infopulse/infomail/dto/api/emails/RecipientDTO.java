package com.infopulse.infomail.dto.api.emails;

import com.infopulse.infomail.models.recipients.Recipient;
import com.infopulse.infomail.models.mail.enums.RecipientType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipientDTO implements Recipient {

	@Email
	private String email;

	@NotBlank
	private RecipientType recipientType;
}