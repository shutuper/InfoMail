package com.infopulse.infomail.dto.securityRequests;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequest {

	private String email;
	private String password;

}
