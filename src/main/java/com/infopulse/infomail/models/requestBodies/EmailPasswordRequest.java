package com.infopulse.infomail.models.requestBodies;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EmailPasswordRequest {

	private String email;
	private String password;

}
