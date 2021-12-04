package com.infopulse.infomail.dto.api.schedule;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginatedScheduledTasksDTO {

	private List<ScheduledTaskDTO> tasks;
	private Long totalNumberOfRows;

}
