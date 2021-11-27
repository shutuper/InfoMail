package com.infopulse.infomail.services.mail;

import com.infopulse.infomail.dto.api.EmailsIdsDTO;
import com.infopulse.infomail.dto.api.HistoryDTO;
import com.infopulse.infomail.models.mail.AppUserEmailsInfo;
import com.infopulse.infomail.models.mail.EmailLog;
import com.infopulse.infomail.models.mail.enums.EmailStatus;
import com.infopulse.infomail.models.mail.enums.RecipientType;
import com.infopulse.infomail.repositories.EmailLogRepository;
import com.infopulse.infomail.services.RecipientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EmailLogService {

//	@Value("${spring.quartz.properties.org.quartz.scheduler.instanceName}")
//	private String SCHEDULER_NAME;

	private final EmailLogRepository emailLogRepository;
	private final EmailSenderService emailSenderService;
	private final RecipientService recipientService;

	public EmailLogService(EmailLogRepository emailLogRepository,
	                       @Lazy EmailSenderService emailSenderService,
	                       RecipientService recipientService) {
		this.emailLogRepository = emailLogRepository;
		this.emailSenderService = emailSenderService;
		this.recipientService = recipientService;
	}

	private static HistoryDTO convertToDto(EmailLog emailLog) {
		return new HistoryDTO(
				emailLog.getId(),
				emailLog.getLogDateTime(),
				emailLog.getEmailStatus().equals(EmailStatus.SENT),
				emailLog.getUserInfo().getEmailTemplate().getSubject()
		);
	}

	public void saveNewEmailLog(AppUserEmailsInfo appUserEmailsInfo,
	                            String message,
	                            EmailStatus emailStatus, String senderEmail) {

		EmailLog emailLog = new EmailLog(
				message,
				LocalDateTime.now(),
				emailStatus,
				appUserEmailsInfo,
				senderEmail);

		emailLogRepository.save(emailLog);
	}

	public EmailLog saveSentEmailLog(EmailLog emailLog) {
		emailLog.setMessage(null);
		emailLog.setEmailStatus(EmailStatus.SENT);
		emailLog.setLogDateTime(LocalDateTime.now());
		return emailLogRepository.save(emailLog);
	}

	public EmailLog saveFailedEmailLog(EmailLog emailLog, String message) {
		emailLog.setMessage(message);
		emailLog.setEmailStatus(EmailStatus.FAILED);
		emailLog.setLogDateTime(LocalDateTime.now());
		return emailLogRepository.save(emailLog);
	}

	@Transactional
	public HistoryDTO retryFailedEmail(Long emailId, String senderEmail) {
		EmailLog emailLog = getEmailLogByIdAndSenderEmail(emailId, senderEmail);

		if (emailLog.getEmailStatus().isSent())
			throw new IllegalStateException(
					String.format("Can't retry sending email with id %s, it's not failed", emailId)
			);

		AppUserEmailsInfo userInfo = emailLog.getUserInfo();
		Map<RecipientType, List<String>> groupedRecipients = recipientService
				.groupRecipients(recipientService.getAllByUserInfoId(userInfo.getId()));

		emailLog = emailSenderService.resendFailedMimeEmail(userInfo.getEmailTemplate(), groupedRecipients, emailLog);
		return EmailLogService.convertToDto(emailLog);
	}


	public Integer getTotalNumberOfUserEmails(String userEmail) {
		return emailLogRepository.countBySenderEmail(userEmail);
	}

	public EmailLog getEmailLogByIdAndSenderEmail(Long emailId, String senderEmail) {
		return emailLogRepository.findByIdAndSenderEmail(emailId, senderEmail).orElseThrow(
				() -> new IllegalStateException(
						String.format("Can't find email with id %s, user: %s", emailId, senderEmail))
		);
	}

	@Transactional
	public void deleteByIdAndUserEmail(Long id, String userEmail) {
		emailLogRepository.deleteByIdAndSenderEmail(id, userEmail);
	}

	@Transactional
	public void deleteAllByIdsAndUserEmail(EmailsIdsDTO ids, String userEmail) {
		emailLogRepository.deleteAllBySenderEmailAndIdIn(userEmail, ids.getIds());
	}

	@Transactional
	public List<HistoryDTO> getPaginatedEmailsHistory(Integer page,
	                                                  Integer rows,
	                                                  Integer sortOrder,
	                                                  String sortField,
	                                                  String senderEmail) {

		Sort sort = Sort.by(sortField);
		sort = sortOrder > 0 ? sort.ascending() : sort.descending();
		Pageable pageable = PageRequest.of(
				page,
				rows,
				sort);

		List<EmailLog> emailLogs = emailLogRepository.findAllBySenderEmail(senderEmail, pageable);
		emailLogs.forEach(System.out::println);
		log.info("User {} requested emails history, page {}, rows {}, sort order {}, sort field {}",
				senderEmail, page, rows, sortOrder, sortField);

		return emailLogsToHistory(emailLogs);
	}

	private List<HistoryDTO> emailLogsToHistory(List<EmailLog> emailLogs) {
		return emailLogs.stream()
				.map(EmailLogService::convertToDto)
				.collect(Collectors.toList());
	}

}
