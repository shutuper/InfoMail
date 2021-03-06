package com.infopulse.infomail.services.mail;

import com.infopulse.infomail.dto.api.templates.EmailTemplateDTO;
import com.infopulse.infomail.dto.api.templates.UserEmailTemplateDTO;
import com.infopulse.infomail.dto.api.templates.UserTemplatesOptionsDTO;
import com.infopulse.infomail.dto.app.IdsDTO;
import com.infopulse.infomail.exceptions.UserEmailTemplateException;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class UserEmailTemplateService {

	private final UserEmailTemplateRepository userEmailTemplateRepository;
	private final List<UserEmailTemplateDTO> basicTemplates = new ArrayList<>(){{
		add(new UserEmailTemplateDTO(
				0L,
				"My template 1",
				"Welcome to Infomail",
				"<b>Welcome to Infomail</b><div>How about scheduling several emails?<br></div>",
				"",
				""
		));
		add(new UserEmailTemplateDTO(
				0L,
				"My template 2",
				"Do you know about Infomail Templates?",
				"<div><span><b>We're so excited that you've decided to create a new template!&nbsp;</b></span><br></div><div><font size=\"4\">Now that you're here, let's make sure you know how to get the most out of Infomail Templates.</font></div><div><font size=\"4\">- Create a new template</font></div><div><font size=\"4\">- Give your template a name</font></div><div><font size=\"4\">- Edit your template</font></div><div><font size=\"4\">- Open and share to your friends or collegues</font></div>",
				"",
				""
		));
	}};

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

	public UserEmailTemplateDTO getEmailTemplateAsDtoById(Long id, String userEmail) {
		return new UserEmailTemplateDTO(getEmailTemplateById(id, userEmail));
	}

	public UserEmailTemplate getTemplateBySharingId(String sharingId, String userEmail) {
		log.info("User {} requested UserEmailTemplate by sharingId: {}", userEmail, sharingId);
		return userEmailTemplateRepository.findBySharingLink(sharingId)
				.orElseThrow(() -> new IllegalStateException(
						String.format("UserEmailTemplate with sharingId %s does not exist", sharingId)
				));
	}

	public UserEmailTemplateDTO getTemplateAsDtoBySharingId(String sharingId, String userEmail) {
		validateSharingId(sharingId);
		return new UserEmailTemplateDTO(getTemplateBySharingId(sharingId, userEmail));
	}

	public void addBasicTemplates(AppUser user) {

		basicTemplates.forEach((emailTemplateDTO) -> {

			log.info("User {} save basic UserEmailTemplate", user.getEmail());
			String shareLink = UUID.randomUUID().toString();

			UserEmailTemplate template = new UserEmailTemplate(
					user,
					emailTemplateDTO.getName(),
					emailTemplateDTO.getSubject(),
					emailTemplateDTO.getBody(),
					shareLink);
			userEmailTemplateRepository.save(template);
		});
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

	public void saveTemplateBySharingId(String sharingId, Authentication authentication) {
		log.info("User {} save shared template by sharing id {}", authentication.getName(), sharingId);

		validateSharingId(sharingId);

		UserEmailTemplate templateBySharingId = getTemplateBySharingId(sharingId, authentication.getName());

		Long userId = (Long) authentication.getCredentials();
		String shareLink = UUID.randomUUID().toString();

		UserEmailTemplate emailTemplate = new UserEmailTemplate(
				new AppUser(userId),
				templateBySharingId.getName(),
				templateBySharingId.getSubject(),
				templateBySharingId.getBody(),
				shareLink);

		userEmailTemplateRepository.save(emailTemplate);
	}

	private void validateSharingId(String sharingId) {
		try {
			UUID.fromString(sharingId);
		} catch (IllegalArgumentException ex) {
			String message = String.format("Template sharing id %s invalid!", sharingId);
			log.error(message);
			throw new UserEmailTemplateException(message);
		}
	}

	@Transactional
	public void deleteByIdAndUserEmail(Long id, String userEmail) {
		log.info("User {} delete UserEmailTemplate by id: {}", userEmail, id);
		userEmailTemplateRepository.deleteByIdAndAppUser_Email(id, userEmail);
	}

	@Transactional
	public void deleteAllByIdsAndUserEmail(IdsDTO ids, String userEmail) {
		log.info("User {} delete UserEmailTemplates by ids: {}", userEmail, ids.getIds().toString());
		userEmailTemplateRepository.deleteAllByAppUser_EmailAndIdIn(userEmail, ids.getIds());
	}
}
