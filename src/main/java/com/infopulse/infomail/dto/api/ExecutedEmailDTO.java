package com.infopulse.infomail.dto.api;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExecutedEmailDTO {

	private Long id;
	private LocalDateTime dateTime;
	private Boolean status;
	private String subject;
}
