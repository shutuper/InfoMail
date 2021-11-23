package com.infopulse.infomail.services.mail;

import com.infopulse.infomail.dto.api.EmailTemplatesIdsDTO;
import com.infopulse.infomail.dto.mail.EmailTemplateDTO;
import com.infopulse.infomail.models.mail.EmailTemplate;
import com.infopulse.infomail.models.users.AppUser;
import com.infopulse.infomail.repositories.EmailTemplateRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class EmailTemplateService {

	private final EmailTemplateRepository emailTemplateRepository;

	public EmailTemplate getEmailTemplateById(Long id, String userEmail) {
		return emailTemplateRepository.findByIdAndAppUser_Email(id,userEmail)
				.orElseThrow(() -> new IllegalStateException(
						String.format("EmailTemplate with id %s does not exist", id)
				));
	}

	@Transactional
	public EmailTemplate saveEmailTemplate(EmailTemplateDTO emailTemplateDTO, Long userId) {
		String shareLink = UUID.randomUUID().toString();

		EmailTemplate emailTemplate = new EmailTemplate(
				new AppUser(userId),
				emailTemplateDTO.getName(),
				emailTemplateDTO.getSubject(),
				emailTemplateDTO.getBody(),
				shareLink);

		return emailTemplateRepository.save(emailTemplate);
	}

	public List<EmailTemplateDTO> getEmailTemplates(String userEmail) {
		List<EmailTemplate> emailTemplates = emailTemplateRepository.findAllByAppUser_Email(userEmail);
		emailTemplates.forEach(System.out::println);
		log.info("User {} requested emailTemplates",
				userEmail);
		return emailTemplates.stream()
				.map(template -> new EmailTemplateDTO(
						template.getId(),
						template.getName(),
						template.getSubject(),
						template.getBody()
				))
				.collect(Collectors.toList());
	}

	@Transactional
	public void deleteByIdAndUserEmail(Long id, String userEmail) {
		log.info(String.valueOf(getEmailTemplateById(id, userEmail)));
		log.info("User {} delete emailTemplate by id: {}", userEmail, id);
		emailTemplateRepository.deleteByIdAndAppUser_Email(id, userEmail);
	}

	@Transactional
	public void deleteAllByIdsAndUserEmail(EmailTemplatesIdsDTO ids, String userEmail) {
		List<EmailTemplate> emailTemplates = emailTemplateRepository.findAllByAppUser_Email(userEmail);
		emailTemplates.forEach((e) -> log.info(e.toString()));
		log.info("User {} delete emailTemplates by ids: {}", userEmail, ids.getIds().toString());
		emailTemplateRepository.deleteAllByAppUser_EmailAndIdIn(userEmail, ids.getIds());
	}
}
