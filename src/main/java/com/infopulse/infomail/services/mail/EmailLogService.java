package com.infopulse.infomail.services.mail;

import com.infopulse.infomail.models.mail.AppUserEmailsInfo;
import com.infopulse.infomail.models.mail.EmailLog;
import com.infopulse.infomail.models.mail.enums.EmailStatus;
import com.infopulse.infomail.models.quartz.QrtzJobDetail;
import com.infopulse.infomail.models.quartz.QrtzJobDetailId;
import com.infopulse.infomail.repositories.EmailLogRepository;
import com.infopulse.infomail.repositories.QrtzJobDetailRepository;
import lombok.AllArgsConstructor;
import org.quartz.JobKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class EmailLogService {

	@Value("${spring.quartz.properties.org.quartz.scheduler.instanceName}")
	private String SCHEDULER_NAME;

	private final EmailLogRepository emailLogRepository;
	private final QrtzJobDetailRepository qrtzJobDetailRepository;

	@Transactional
	public void saveEmailLog(AppUserEmailsInfo appUserEmailsInfo,
	                         String message,
	                         EmailStatus emailStatus) {

		EmailLog emailLog = new EmailLog(
				message,
				LocalDateTime.now(),
				emailStatus,
				appUserEmailsInfo);

		emailLogRepository.save(emailLog);
	}

}
