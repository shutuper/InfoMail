package com.infopulse.infomail.repositories;

import com.infopulse.infomail.dto.app.ScheduledTaskFullRaw;
import com.infopulse.infomail.dto.app.ScheduledTaskRaw;
import com.infopulse.infomail.models.quartz.QrtzJobDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface QrtzJobDetailRepository extends JpaRepository<QrtzJobDetail, String> {

	@Transactional
//	@Query(value = "select q from qrtz_job_details q where q.job_name=?1", nativeQuery = true)
	Optional<QrtzJobDetail> findByJobName(String jobName);

	@Query(value = """
			select
				jobs.job_name as jobName,
				jobs.order_id as orderId,
				jobs.description as description,
				trigs.trigger_state as triggerState,
				trigs.start_time as startAt,
				trigs.end_time as endAt,
				temp.subject as subject
			from
			    (qrtz_job_details jobs inner join qrtz_triggers trigs on jobs.job_name = trigs.job_name
			     inner join app_user_emails_info info on jobs.job_name = info.qrtz_job_detail_id
			     inner join email_template temp on info.email_template_id = temp.id)
			where
			    jobs.job_group = ?1
			""",
			countQuery = "select count(job.order_id) from qrtz_job_details job where job.job_group = ?1",
			nativeQuery = true)
	Page<ScheduledTaskFullRaw> getAllDTObyGroup(String jobGroup, Pageable sortByAndPage);

	@Query(value = """
			select
				jobs.job_name as jobName,
				jobs.order_id as orderId,
				jobs.description as description,
				trigs.trigger_state as triggerState,
				trigs.start_time as startAt,
				trigs.end_time as endAt,
				temp.subject as subject,
				temp.body as body
			from
			    (qrtz_job_details jobs inner join qrtz_triggers trigs on jobs.job_name = trigs.job_name
			     inner join app_user_emails_info info on jobs.job_name = info.qrtz_job_detail_id
			     inner join email_template temp on info.email_template_id = temp.id)
			where
			    jobs.job_name = ?1 and jobs.job_group = ?2
			""",
			nativeQuery = true)
	Optional<ScheduledTaskFullRaw> getDTOByJobName(String jobName, String jobGroup);

	void deleteAllByJobNameInAndJobGroup(List<String> jobNames, String jobGroup);

	void deleteByJobNameAndJobGroup(String jobName, String jobGroup);

}
