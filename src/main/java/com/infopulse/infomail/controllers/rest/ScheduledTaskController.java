package com.infopulse.infomail.controllers.rest;

import com.infopulse.infomail.dto.api.schedule.JobNamesDTO;
import com.infopulse.infomail.dto.api.schedule.PaginatedScheduledTasksDTO;
import com.infopulse.infomail.dto.api.schedule.ScheduledTaskWithEmailDTO;
import com.infopulse.infomail.services.scheduler.tasks.ScheduledTasksService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@SecurityRequirement(name = "Authorization")
@RequestMapping("/api/v1/tasks")
@AllArgsConstructor
public class ScheduledTaskController {

	private final ScheduledTasksService taskService;

	@GetMapping
	public ResponseEntity<PaginatedScheduledTasksDTO> getAllUserScheduledTasks(@RequestParam("page") Integer page,
	                                                                           @RequestParam("rows") Integer rows,
	                                                                           @RequestParam("sortOrder") Integer sortOrder,
	                                                                           @RequestParam("sortField") String sortField,
	                                                                           Authentication authentication) {
		String jobGroup = authentication.getName();

		return ResponseEntity.ok(taskService
				.getUserPaginatedTasks(page, rows, sortOrder, sortField, jobGroup));
	}

	@GetMapping("{jobName}/dto")
	public ResponseEntity<ScheduledTaskWithEmailDTO> getTaskDtoByJobName(@PathVariable("jobName") String jobName,
	                                                                     Authentication authentication) {
		String jobGroup = authentication.getName();

		return ResponseEntity.ok(taskService
				.getTaskDtoByJobName(jobName, jobGroup));
	}


	@PatchMapping("resume/{jobName}")
	public ResponseEntity<?> resumeJob(@PathVariable("jobName") String jobName,
	                                   Authentication authentication) {
		String jobGroup = authentication.getName();

		taskService.resumeJob(jobName, jobGroup);
		return ResponseEntity.ok().build();
	}


	@PatchMapping("pause/{jobName}")
	public ResponseEntity<?> pauseJob(@PathVariable("jobName") String jobName,
	                                  Authentication authentication) {
		String jobGroup = authentication.getName();

		taskService.pauseJob(jobName, jobGroup);
		return ResponseEntity.ok().build();
	}

	@PatchMapping("pauseAll")
	public ResponseEntity<?> pauseAllUserJobs(Authentication authentication) {
		String jobGroup = authentication.getName();

		taskService.pauseAllUserJobs(jobGroup);
		return ResponseEntity.ok().build();
	}

	@PatchMapping("resumeAll")
	public ResponseEntity<?> resumeAllUserJobs(Authentication authentication) {
		String jobGroup = authentication.getName();

		taskService.resumeAllUserJobs(jobGroup);
		return ResponseEntity.ok().build();
	}


	@DeleteMapping()
	public ResponseEntity<?> deleteAllJobsByNames(@Valid @RequestBody JobNamesDTO jobNamesDTO,
	                                              Authentication authentication) {
		String jobGroup = authentication.getName();

		taskService.deleteAllByNames(jobNamesDTO.getJobNames(), jobGroup);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("{jobName}")
	public ResponseEntity<?> deleteJobByNameAndGroup(@PathVariable("jobName") String jobName,
	                                                 Authentication authentication) {
		String jobGroup = authentication.getName();

		taskService.deleteJob(jobName, jobGroup);
		return ResponseEntity.ok().build();
	}


}
