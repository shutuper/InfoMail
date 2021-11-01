package com.infopulse.infomail.dto;

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
