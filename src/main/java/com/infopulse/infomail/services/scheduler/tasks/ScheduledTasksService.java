package com.infopulse.infomail.services.scheduler.tasks;

import com.infopulse.infomail.dto.api.emails.RecipientDTO;
import com.infopulse.infomail.dto.api.schedule.PaginatedScheduledTasksDTO;
import com.infopulse.infomail.dto.api.schedule.ScheduledTaskWithEmailDTO;
import com.infopulse.infomail.dto.app.ScheduledTaskRaw;
import com.infopulse.infomail.services.mail.RecipientService;
import com.infopulse.infomail.services.scheduler.QrtzJobDetailService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class ScheduledTasksService {

	private final QrtzJobDetailService jobDetailService;
	private final Scheduler scheduler;
	private final RecipientService recipientService;

	@Transactional
	public PaginatedScheduledTasksDTO getUserPaginatedTasks(Integer page,
	                                                        Integer rows,
	                                                        Integer sortOrder,
	                                                        String sortField,
	                                                        String jobGroup) {
		Sort sort = Sort.by(sortField);
		sort = sortOrder > 0 ? sort.ascending() : sort.descending();
		Pageable pageable = PageRequest.of(page, rows, sort);

		PaginatedScheduledTasksDTO paginatedScheduledTasksDTO = jobDetailService
				.getAllScheduledTaskDTObyGroup(jobGroup, pageable);

		log.info("User {} requested scheduled tasks, page {}, rows {}, sort order {}, sort field {}",
				jobGroup, page, rows, sortOrder, sortField);

		return paginatedScheduledTasksDTO;
	}

	@Transactional
	public ScheduledTaskWithEmailDTO getTaskDtoByJobName(String jobName, String jobGroup) {
		log.info("User {} requested scheduled task with jobName: {}", jobGroup, jobName);

		final ScheduledTaskRaw scheduledTask = jobDetailService
				.getScheduledTaskFullRawByJobName(jobName, jobGroup);
		final List<RecipientDTO> recipients = recipientService
				.getAllAsDTOByJobName(jobName);

		return new ScheduledTaskWithEmailDTO(scheduledTask, recipients);
	}


	@Transactional
	public void pauseJob(String jobName, String jobGroup) throws SchedulerException {
		scheduler.pauseJob(generateJobKey(jobName, jobGroup));
		log.info("Job {} is paused", jobName);
	}

	@Transactional
	public void resumeJob(String jobName, String jobGroup) throws SchedulerException {
		scheduler.resumeJob(generateJobKey(jobName, jobGroup));
		log.info("Job {} is resumed", jobName);
	}

	@Transactional
	public void resumeAllUserJobs(String jobGroup) throws SchedulerException {
		scheduler.resumeJobs(GroupMatcher.groupEquals(jobGroup));
		log.info("Job with group {} are resumed", jobGroup);
	}

	@Transactional
	public void pauseAllUserJobs(String jobGroup) throws SchedulerException {
		scheduler.pauseJobs(GroupMatcher.groupEquals(jobGroup));
		log.info("Job with group {} are paused", jobGroup);
	}

	@Transactional
	public void deleteJob(String jobName, String jobGroup) {
		validateUUID(jobName);
		jobDetailService
				.deleteByJobNameAndGroup(jobName, jobGroup);
		log.info("Job {} is deleted", jobName);
	}

	@Transactional
	public void deleteAllByNames(List<String> jobNames, String jobGroup) {
		jobDetailService
				.deleteAllByNamesAndGroup(jobNames, jobGroup);
		log.info("Jobs {} are deleted", Arrays.toString(jobNames.toArray()));

	}

	private JobKey generateJobKey(String jobName, String jobGroup) {
		validateUUID(jobName);
		return new JobKey(jobName, jobGroup);
	}

	@SuppressWarnings("ResultOfMethodCallIgnored")
	private void validateUUID(String uuid) {
		UUID.fromString(uuid);
	}

}
