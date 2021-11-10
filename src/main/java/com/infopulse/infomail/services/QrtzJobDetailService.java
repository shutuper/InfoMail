package com.infopulse.infomail.services;

import com.infopulse.infomail.models.quartz.QrtzJobDetail;
import com.infopulse.infomail.repositories.QrtzJobDetailRepository;
import com.infopulse.infomail.services.scheduler.jobs.EmailSendJob;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.springframework.stereotype.Service;

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

	public QrtzJobDetail createNewQrtzJobDetail(JobDetail jobDetail) {
		JobKey jobKey = jobDetail.getKey();
		QrtzJobDetail qrtzJobDetail = new QrtzJobDetail(
				jobKey.getName(),
				jobKey.getGroup(),
				"quartzScheduler",
				jobDetail.getDescription(),
				jobDetail.getJobClass().getName(),
				jobDetail.isDurable(),
				false, false, false, null);
		return qrtzJobDetailRepository.saveAndFlush(qrtzJobDetail);
	}

}
