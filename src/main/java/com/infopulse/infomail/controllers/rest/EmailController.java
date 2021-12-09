package com.infopulse.infomail.controllers.rest;

import com.infopulse.infomail.dto.api.emails.EmailDTO;
import com.infopulse.infomail.dto.api.templates.EmailTemplateDTO;
import com.infopulse.infomail.dto.api.emails.RecipientDTO;
import com.infopulse.infomail.models.schedule.EmailSchedule;
import com.infopulse.infomail.services.scheduler.CronSchedulerService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/emails")
public class EmailController {

	private final CronSchedulerService cronSchedulerService;

	@PostMapping
	public ResponseEntity<EmailDTO> createEmailJob(@Valid @RequestBody EmailDTO emailDTO, Authentication authentication) {
		try {
			List<RecipientDTO> recipients = emailDTO.getRecipients();
			EmailTemplateDTO emailTemplateDTO = emailDTO.getEmailTemplate();
			EmailSchedule emailSchedule = EmailSchedule.fromDTO(emailDTO.getEmailSchedule());
			String userEmail = (String) authentication.getPrincipal();
			Long userId = (Long) authentication.getCredentials();

			cronSchedulerService.createTask(recipients, emailTemplateDTO, emailSchedule, userEmail, userId);
			return new ResponseEntity<>(emailDTO, HttpStatus.CREATED);

		} catch (Exception e) {
			return new ResponseEntity<>(emailDTO, HttpStatus.BAD_REQUEST);
		}

	}

}
