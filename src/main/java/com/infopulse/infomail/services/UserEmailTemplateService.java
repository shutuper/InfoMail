package com.infopulse.infomail.services;

import com.infopulse.infomail.dto.mail.EmailTemplateDTO;
import com.infopulse.infomail.models.mail.EmailTemplate;
import com.infopulse.infomail.models.mail.UserEmailTemplate;
import com.infopulse.infomail.models.users.AppUser;
import com.infopulse.infomail.repositories.EmailTemplateRepository;
import com.infopulse.infomail.repositories.UserEmailTemplateRepository;
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
public class UserEmailTemplateService {

	private final UserEmailTemplateRepository userEmailTemplateRepository;

	public UserEmailTemplate getEmailTemplateById(Long id, String userEmail) {
		return userEmailTemplateRepository.findByIdAndAppUser_Email(id, userEmail)
				.orElseThrow(() -> new IllegalStateException(
						String.format("EmailTemplate with id %s does not exist", id)
				));
	}

	@Transactional
	public UserEmailTemplate saveEmailTemplate(EmailTemplateDTO emailTemplateDTO, Long userId) {
		String shareLink = UUID.randomUUID().toString();

		UserEmailTemplate emailTemplate = new UserEmailTemplate(
				new AppUser(userId),
				emailTemplateDTO.getName(),
				emailTemplateDTO.getSubject(),
				emailTemplateDTO.getBody(),
				shareLink);

		return userEmailTemplateRepository.save(emailTemplate);
	}

	public List<EmailTemplateDTO> getEmailTemplates(String userEmail) {
		List<UserEmailTemplate> emailTemplates = userEmailTemplateRepository.findAllByAppUser_Email(userEmail);
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
}
