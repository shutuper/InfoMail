package com.infopulse.infomail.services.mail;

import com.infopulse.infomail.dto.api.EmailTemplatesIdsDTO;
import com.infopulse.infomail.dto.mail.UserEmailTemplateDTO;
import com.infopulse.infomail.models.mail.UserEmailTemplate;
import com.infopulse.infomail.models.users.AppUser;
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
	public UserEmailTemplate saveEmailTemplate(UserEmailTemplateDTO emailTemplateDTO, Long userId) {
		String shareLink = UUID.randomUUID().toString();

		UserEmailTemplate emailTemplate = new UserEmailTemplate(
				new AppUser(userId),
				emailTemplateDTO.getName(),
				emailTemplateDTO.getSubject(),
				emailTemplateDTO.getBody(),
				shareLink);

		return userEmailTemplateRepository.save(emailTemplate);
	}

	public List<UserEmailTemplateDTO> getEmailTemplates(String userEmail) {
		List<UserEmailTemplate> emailTemplates = userEmailTemplateRepository.findAllByAppUser_Email(userEmail);
		emailTemplates.forEach(System.out::println);
		log.info("User {} requested emailTemplates",
				userEmail);
		return emailTemplates.stream()
				.map(template -> new UserEmailTemplateDTO(
						template.getId(),
						template.getName(),
						template.getSubject(),
						template.getBody(),
						userEmail,
						template.getSharingLink()
				))
				.collect(Collectors.toList());
	}

	@Transactional
	public void deleteByIdAndUserEmail(Long id, String userEmail) {
		log.info(String.valueOf(getEmailTemplateById(id, userEmail)));
		log.info("User {} delete emailTemplate by id: {}", userEmail, id);
		userEmailTemplateRepository.deleteByIdAndAppUser_Email(id, userEmail);
	}

	@Transactional
	public void deleteAllByIdsAndUserEmail(EmailTemplatesIdsDTO ids, String userEmail) {
		List<UserEmailTemplate> emailTemplates = userEmailTemplateRepository.findAllByAppUser_Email(userEmail);
		emailTemplates.forEach((e) -> log.info(e.toString()));
		log.info("User {} delete userEmailTemplates by ids: {}", userEmail, ids.getIds().toString());
		userEmailTemplateRepository.deleteAllByAppUser_EmailAndIdIn(userEmail, ids.getIds());
	}
}
