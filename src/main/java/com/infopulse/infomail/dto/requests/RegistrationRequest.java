package com.infopulse.infomail.dto.requests;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class RegistrationRequest {

	@Email
	private final String email;
	@Length(min = 8, max = 24)
	private final String password;

}
