package com.infopulse.infomail.dto.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HistoryDTO {

	private Long id;
	private LocalDateTime dateTime;
	private Boolean status;
	private String subject;

}
