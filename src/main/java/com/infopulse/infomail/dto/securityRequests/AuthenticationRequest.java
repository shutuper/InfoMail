package com.infopulse.infomail.dto.securityRequests;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AuthenticationRequest {

	private String email;
	private String password;

}
