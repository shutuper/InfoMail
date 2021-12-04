package com.infopulse.infomail.services.mail;

import com.infopulse.infomail.dto.api.templates.EmailTemplateDTO;
import com.infopulse.infomail.models.templates.EmailTemplate;
import com.infopulse.infomail.models.templates.UserEmailTemplate;
import com.infopulse.infomail.models.users.AppUser;
import com.infopulse.infomail.repositories.templates.EmailTemplateRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

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
			log.info("Template with id: {} saved from existed one", userEmailTemplate.getId());
			emailTemplate = new EmailTemplate(userEmailTemplate);
		} else {
			validate(emailTemplateDTO);
			emailTemplate = new EmailTemplate(
					new AppUser(userId),
					emailTemplateDTO.getSubject(),
					emailTemplateDTO.getBody());
		}
		return emailTemplateRepository.save(emailTemplate);
	}

	public EmailTemplate getEmailTemplateById(Long id, String userEmail) {
		log.info("User {} requested EmailTemplate with id: {}", userEmail, id);
		return emailTemplateRepository.findByIdAndAppUser_Email(id, userEmail)
				.orElseThrow(() -> new IllegalStateException(
						String.format("EmailTemplate with id %s does not exist", id)
				));
	}

	public EmailTemplateDTO getEmailTemplateAsDtoById(Long id, String userEmail) {
		return getEmailTemplateById(id, userEmail).toDto();
	}


	private void validate(EmailTemplateDTO emailTemplateDTO) {
		if (Objects.isNull(emailTemplateDTO.getBody()) || Objects.isNull(emailTemplateDTO.getSubject()))
			throw new IllegalStateException("EmailTemplateDTO body and subject should not be blank!");
	}
}
