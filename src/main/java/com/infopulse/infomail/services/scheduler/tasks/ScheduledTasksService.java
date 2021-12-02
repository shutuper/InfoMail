package com.infopulse.infomail.services.scheduler.tasks;

import com.infopulse.infomail.dto.api.ExecutedEmailDTO;
import com.infopulse.infomail.dto.api.PaginatedScheduledTasksDTO;
import com.infopulse.infomail.dto.api.ScheduledTaskDTO;
import com.infopulse.infomail.models.mail.EmailLog;
import com.infopulse.infomail.services.QrtzJobDetailService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class ScheduledTasksService {

	private final QrtzJobDetailService jobDetailService;

	@Transactional
	public PaginatedScheduledTasksDTO getUserPaginatedTasks(Integer page,
	                                                    Integer rows,
	                                                    Integer sortOrder,
	                                                    String sortField,
	                                                    String jobGroup) {
		Sort sort = Sort.by(sortField);
		sort = sortOrder > 0 ? sort.ascending() : sort.descending();
		Pageable pageable = PageRequest.of(
				page,
				rows,
				sort);

		PaginatedScheduledTasksDTO paginatedScheduledTasksDTO = jobDetailService.getAllScheduledTaskDTObyGroup(jobGroup, pageable);

		log.info("User {} requested emails history, page {}, rows {}, sort order {}, sort field {}",
				jobGroup, page, rows, sortOrder, sortField);

		return paginatedScheduledTasksDTO;
	}


}
