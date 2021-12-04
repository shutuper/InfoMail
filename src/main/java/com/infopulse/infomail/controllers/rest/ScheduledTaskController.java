package com.infopulse.infomail.controllers.rest;

import com.infopulse.infomail.dto.api.schedule.PaginatedScheduledTasksDTO;
import com.infopulse.infomail.dto.api.schedule.ScheduledTaskFullDTO;
import com.infopulse.infomail.services.scheduler.tasks.ScheduledTasksService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/tasks")
public class ScheduledTaskController {

	private final ScheduledTasksService taskService;

	@GetMapping
	public ResponseEntity<PaginatedScheduledTasksDTO> getAllUserScheduledTasks(@RequestParam("page") Integer page,
	                                                                           @RequestParam("rows") Integer rows,
	                                                                           @RequestParam("sortOrder") Integer sortOrder,
	                                                                           @RequestParam("sortField") String sortField,
	                                                                           Authentication authentication) {
		try {
			String jobGroup = authentication.getName();

			return ResponseEntity.ok(taskService
					.getUserPaginatedTasks(page, rows, sortOrder, sortField, jobGroup));
		} catch (Exception ex) {
			return ResponseEntity.badRequest().build();
		}

	}

	@GetMapping("{jobName}/dto")
	public ResponseEntity<ScheduledTaskFullDTO> getTaskDtoByJobName(@PathVariable("jobName") String jobName, Authentication authentication) {
		try {
			String jobGroup = authentication.getName();

			return ResponseEntity.ok(taskService
					.getTaskDtoByJobName(jobName, jobGroup));
		} catch (Exception ex) {
			return ResponseEntity.badRequest().build();
		}

	}


}
