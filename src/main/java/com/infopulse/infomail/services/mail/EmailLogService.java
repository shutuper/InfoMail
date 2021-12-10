package com.infopulse.infomail.services.mail;

import com.infopulse.infomail.dto.api.emails.ExecutedEmailDTO;
import com.infopulse.infomail.dto.api.emails.RecipientDTO;
import com.infopulse.infomail.dto.api.templates.EmailTemplateDTO;
import com.infopulse.infomail.dto.api.templates.EmailWithTemplateDTO;
import com.infopulse.infomail.dto.app.IdsDTO;
import com.infopulse.infomail.models.mail.AppUserEmailsInfo;
import com.infopulse.infomail.models.mail.EmailLog;
import com.infopulse.infomail.models.mail.enums.EmailStatus;
import com.infopulse.infomail.models.mail.enums.RecipientType;
import com.infopulse.infomail.repositories.EmailLogRepository;
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

	private final EmailLogRepository emailLogRepository;
	private final EmailSenderService emailSenderService;
	private final RecipientService recipientService;


	public EmailLogService(EmailLogRepository emailLogRepository,
	                       // @Lazy because of cycle dependency
	                       @Lazy EmailSenderService emailSenderService,
	                       RecipientService recipientService) {
		this.emailLogRepository = emailLogRepository;
		this.emailSenderService = emailSenderService;
		this.recipientService = recipientService;
	}


	private static ExecutedEmailDTO convertToDto(EmailLog emailLog) {
		return new ExecutedEmailDTO(
				emailLog.getId(),
				emailLog.getLogDateTime(),
				emailLog.getEmailStatus().equals(EmailStatus.SENT),
				emailLog.getUserInfo().getEmailTemplate().getSubject()
		);
	}

	public EmailWithTemplateDTO getEmailWithTemplateDTO(Long emailId, String senderEmail) {
		EmailLog emailLog = getEmailLogByIdAndSenderEmail(emailId, senderEmail);
		AppUserEmailsInfo userInfo = emailLog.getUserInfo();

		ExecutedEmailDTO email = EmailLogService.convertToDto(emailLog);
		EmailTemplateDTO template = new EmailTemplateDTO(userInfo.getEmailTemplate());

		List<RecipientDTO> recipients = recipientService
				.getAllAsDTOByUserInfoId(userInfo.getId());

		EmailWithTemplateDTO emailWithTemplateDTO = new EmailWithTemplateDTO(email, template, recipients);
		log.info("User {} requested: {}", senderEmail, emailWithTemplateDTO);
		return emailWithTemplateDTO;
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

		log.info("User {} saved new emailLog: {}", senderEmail, emailLog);
		emailLogRepository.save(emailLog);
	}

	public EmailLog saveSentEmailLog(EmailLog emailLog) {
		emailLog.setMessage(null);
		emailLog.setEmailStatus(EmailStatus.SENT);
		emailLog.setLogDateTime(LocalDateTime.now());

		log.info("Successfully resent email: {}", emailLog);
		return emailLogRepository.save(emailLog);
	}

	public EmailLog saveFailedEmailLog(EmailLog emailLog, String message) {
		emailLog.setMessage(message);
		emailLog.setEmailStatus(EmailStatus.FAILED);
		emailLog.setLogDateTime(LocalDateTime.now());

		log.error("Retrying of sending email: {} failed", emailLog);
		return emailLogRepository.save(emailLog);
	}

	@Transactional
	public ExecutedEmailDTO retryFailedEmail(Long emailId, String senderEmail) {
		EmailLog emailLog = getEmailLogByIdAndSenderEmail(emailId, senderEmail);

		if (emailLog.getEmailStatus().isSent())
			throw new IllegalStateException(
					String.format("Can't retry sending email with id %s, it's not failed", emailId)
			);

		AppUserEmailsInfo userInfo = emailLog.getUserInfo();
		Map<RecipientType, List<String>> groupedRecipients = recipientService
				.groupRecipients(recipientService.getAllByUserInfoId(userInfo.getId()));

		emailLog = emailSenderService.resendFailedMimeEmail(
				userInfo.getEmailTemplate(),
				groupedRecipients,
				emailLog);

		return EmailLogService.convertToDto(emailLog);
	}


	public Integer getTotalNumberOfUserEmails(String userEmail) {
		log.info("User {} requested total number of hsi templates", userEmail);
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
		emailLogRepository
				.deleteByIdAndSenderEmail(id, userEmail);
		log.info("User {} deleted email with id: {}", userEmail, id);
	}

	@Transactional
	public void deleteAllByIdsAndUserEmail(IdsDTO ids, String userEmail) {
		emailLogRepository
				.deleteAllBySenderEmailAndIdIn(userEmail, ids.getIds());
		log.info("User {} deleted all emails which are in: {}", userEmail, ids);
	}

	@Transactional
	public List<ExecutedEmailDTO> getPaginatedEmailsHistory(Integer page,
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

		log.info("User {} requested emails history, page {}, rows {}, sort order {}, sort field {}",
				senderEmail, page, rows, sortOrder, sortField);

		return emailLogsToExecutedEmails(emailLogs);
	}

	private List<ExecutedEmailDTO> emailLogsToExecutedEmails(List<EmailLog> emailLogs) {
		return emailLogs.stream()
				.map(EmailLogService::convertToDto)
				.collect(Collectors.toList());
	}

}
