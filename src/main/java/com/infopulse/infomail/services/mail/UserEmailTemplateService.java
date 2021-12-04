package com.infopulse.infomail.services.mail;

import com.infopulse.infomail.dto.api.templates.EmailTemplatesIdsDTO;
import com.infopulse.infomail.dto.api.templates.UserTemplatesOptionsDTO;
import com.infopulse.infomail.dto.api.templates.EmailTemplateDTO;
import com.infopulse.infomail.dto.api.templates.UserEmailTemplateDTO;
import com.infopulse.infomail.models.templates.UserEmailTemplate;
import com.infopulse.infomail.models.users.AppUser;
import com.infopulse.infomail.repositories.templates.UserEmailTemplateRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class UserEmailTemplateService {

	private final UserEmailTemplateRepository userEmailTemplateRepository;

	public List<UserEmailTemplateDTO> getPaginatedTemplates(Integer page,
	                                                        Integer rows,
	                                                        Integer sortOrder,
	                                                        String sortField,
	                                                        String userEmail) {

		Sort sort = Sort.by(sortField);
		sort = sortOrder > 0 ? sort.ascending() : sort.descending();
		Pageable pageable = PageRequest.of(
				page,
				rows,
				sort);

		List<UserEmailTemplate> emailTemplates = userEmailTemplateRepository.findAllByAppUser_Email(userEmail, pageable);
		emailTemplates.forEach(System.out::println);
		log.info("User {} requested UserEmailTemplates, page {}, rows {}, sort order {}, sort field {}", userEmail, page, rows, sortOrder, sortField);
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


	public EmailTemplateDTO getEmailTemplateDTO(Long templateId, String userName) {
		UserEmailTemplate template = getEmailTemplateById(templateId, userName);
		return new EmailTemplateDTO(template);
	}

	public List<UserTemplatesOptionsDTO> getAllTemplatesAsOptions(String userEmail) {
		List<UserEmailTemplate> templates = userEmailTemplateRepository.findAllByAppUser_Email(userEmail);
		return templates.stream()
				.map(UserTemplatesOptionsDTO::new).toList();
	}

	public Integer getTotalNumberOfTemplates(String userEmail) {
		log.info("User {} requested total number of his templates", userEmail);
		return userEmailTemplateRepository.countByAppUser_Email(userEmail);
	}

	public UserEmailTemplate getEmailTemplateById(Long id, String userEmail) {
		log.info("User {} requested UserEmailTemplate by id: {}", userEmail, id);
		return userEmailTemplateRepository.findByIdAndAppUser_Email(id, userEmail)
				.orElseThrow(() -> new IllegalStateException(
						String.format("UserEmailTemplate with id %s does not exist", id)
				));
	}

	public UserEmailTemplate getTemplateBySharingId(String sharingId, String userEmail) {
		log.info("User {} requested UserEmailTemplate by sharingId: {}", userEmail, sharingId);
		return userEmailTemplateRepository.findBySharingLink(sharingId)
				.orElseThrow(() -> new IllegalStateException(
						String.format("UserEmailTemplate with sharingId %s does not exist", sharingId)
				));
	}

	@Transactional
	public UserEmailTemplateDTO saveEmailTemplate(UserEmailTemplateDTO emailTemplateDTO, Authentication authentication) {
		String userEmail = authentication.getName();
		if (Objects.nonNull(emailTemplateDTO.getId())) {
			return updateEmailTemplate(emailTemplateDTO, authentication);
		}

		log.info("User {} save new UserEmailTemplate", authentication.getName());
		Long userId = (Long) authentication.getCredentials();
		String shareLink = UUID.randomUUID().toString();

		UserEmailTemplate template = new UserEmailTemplate(
				new AppUser(userId),
				emailTemplateDTO.getName(),
				emailTemplateDTO.getSubject(),
				emailTemplateDTO.getBody(),
				shareLink);

		template = userEmailTemplateRepository.save(template);
		return new UserEmailTemplateDTO(
				template.getId(),
				template.getName(),
				template.getSubject(),
				template.getBody(),
				userEmail,
				template.getSharingLink()
		);
	}

	@Transactional
	public UserEmailTemplateDTO updateEmailTemplate(UserEmailTemplateDTO templateDTO, Authentication authentication) {
		String userEmail = authentication.getName();
		long templateId = templateDTO.getId();

		log.info("User {} update UserEmailTemplate by id: {}", userEmail, templateId);
		UserEmailTemplate templateFromDb = getEmailTemplateById(templateId, userEmail);

		templateFromDb.setName(templateDTO.getName());
		templateFromDb.setSubject(templateDTO.getSubject());
		templateFromDb.setBody(templateDTO.getBody());

		templateFromDb = userEmailTemplateRepository.save(templateFromDb);
		return new UserEmailTemplateDTO(
				templateFromDb.getId(),
				templateFromDb.getName(),
				templateFromDb.getSubject(),
				templateFromDb.getBody(),
				userEmail,
				templateFromDb.getSharingLink()
		);
	}

	@Transactional
	public UserEmailTemplate saveSharedTemplate(UserEmailTemplateDTO emailTemplateDTO, Authentication authentication) {
		log.info("User {} save shared template as new UserEmailTemplate", authentication.getName());
		Long userId = (Long) authentication.getCredentials();
		String shareLink = UUID.randomUUID().toString();

		UserEmailTemplate emailTemplate = new UserEmailTemplate(
				new AppUser(userId),
				emailTemplateDTO.getName(),
				emailTemplateDTO.getSubject(),
				emailTemplateDTO.getBody(),
				shareLink);

		return userEmailTemplateRepository.save(emailTemplate);
	}

	@Transactional
	public void deleteByIdAndUserEmail(Long id, String userEmail) {
		log.info(String.valueOf(getEmailTemplateById(id, userEmail)));
		log.info("User {} delete UserEmailTemplate by id: {}", userEmail, id);
		userEmailTemplateRepository.deleteByIdAndAppUser_Email(id, userEmail);
	}

	@Transactional
	public void deleteAllByIdsAndUserEmail(EmailTemplatesIdsDTO ids, String userEmail) {
		log.info("User {} delete UserEmailTemplates by ids: {}", userEmail, ids.getIds().toString());
		userEmailTemplateRepository.deleteAllByAppUser_EmailAndIdIn(userEmail, ids.getIds());
	}
}
