package com.infopulse.infomail.services.mail;

import com.infopulse.infomail.dto.mail.EmailTemplateDTO;
import com.infopulse.infomail.models.mail.EmailTemplate;
import com.infopulse.infomail.models.mail.UserEmailTemplate;
import com.infopulse.infomail.models.users.AppUser;
import com.infopulse.infomail.repositories.EmailTemplateRepository;
import com.infopulse.infomail.repositories.UserEmailTemplateRepository;
import com.infopulse.infomail.services.UserEmailTemplateService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class EmailTemplateService {

	private final EmailTemplateRepository emailTemplateRepository;
	private final UserEmailTemplateService userEmailTemplateService;

	@Transactional
	public EmailTemplate saveEmailTemplate(EmailTemplateDTO emailTemplateDTO, Long userId, String userEmail) {
		EmailTemplate emailTemplate;
		if (Objects.nonNull(emailTemplateDTO.getId())) {
			UserEmailTemplate userEmailTemplate = userEmailTemplateService
					.getEmailTemplateById(emailTemplateDTO.getId(), userEmail);

			emailTemplate = new EmailTemplate(userEmailTemplate);
		} else {
			emailTemplate = new EmailTemplate(
					new AppUser(userId),
					emailTemplateDTO.getSubject(),
					emailTemplateDTO.getBody());
		}
		return emailTemplateRepository.save(emailTemplate);
	}

}
