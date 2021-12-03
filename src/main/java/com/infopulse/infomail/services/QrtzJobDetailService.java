package com.infopulse.infomail.services;

import com.infopulse.infomail.dto.api.PaginatedScheduledTasksDTO;
import com.infopulse.infomail.dto.api.ScheduledTaskDTO;
import com.infopulse.infomail.dto.app.ScheduledTaskRaw;
import com.infopulse.infomail.models.quartz.QrtzJobDetail;
import com.infopulse.infomail.repositories.QrtzJobDetailRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class QrtzJobDetailService {

	private final QrtzJobDetailRepository qrtzJobDetailRepository;


	public QrtzJobDetail findQrtzJobDetailByJobKey(JobKey jobKey) {
		String jobName = jobKey.getName();

		return qrtzJobDetailRepository.findByJobName(jobName).orElseThrow(
				() -> new IllegalStateException(String.format("Job %s does not exist", jobName))
		);
	}

	public PaginatedScheduledTasksDTO getAllScheduledTaskDTObyGroup(String jobGroup, Pageable sortByAndPage) {
		Page<ScheduledTaskRaw> tasksRaw = qrtzJobDetailRepository
				.getAllDTObyGroup(jobGroup, sortByAndPage);

		List<ScheduledTaskDTO> scheduledTaskDTOS = tasksRaw.get().map(ScheduledTaskDTO::new).toList();

		return new PaginatedScheduledTasksDTO(scheduledTaskDTOS, tasksRaw.getTotalElements());
	}

}
