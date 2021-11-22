package com.infopulse.infomail.services.mail;

import com.infopulse.infomail.dto.api.EmailsIdsDTO;

import com.infopulse.infomail.dto.api.HistoryDTO;
import com.infopulse.infomail.models.mail.AppUserEmailsInfo;
import com.infopulse.infomail.models.mail.EmailLog;
import com.infopulse.infomail.models.mail.enums.EmailStatus;
import com.infopulse.infomail.repositories.EmailLogRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class EmailLogService {

//	@Value("${spring.quartz.properties.org.quartz.scheduler.instanceName}")
//	private String SCHEDULER_NAME;

	private final EmailLogRepository emailLogRepository;

	@Transactional
	public void saveEmailLog(AppUserEmailsInfo appUserEmailsInfo,
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

	public Integer getTotalNumberOfUserEmails(String userEmail) {
		return emailLogRepository.countBySenderEmail(userEmail);
	}


	@Transactional
	public void deleteByIdAndUserEmail(Long id, String userEmail) {
		log.info(String.valueOf(emailLogRepository.findByIdAndSenderEmail(id, userEmail).orElseThrow()));
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
				.map(email -> new HistoryDTO(
						email.getId(),
						email.getLogDateTime(),
						email.getEmailStatus().equals(EmailStatus.SENT),
						email.getUserInfo().getEmailTemplate().getSubject()
				))
				.collect(Collectors.toList());
	}

}
