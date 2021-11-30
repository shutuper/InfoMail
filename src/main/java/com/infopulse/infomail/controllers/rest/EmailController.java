package com.infopulse.infomail.controllers.rest;

import com.infopulse.infomail.dto.mail.EmailDTO;
import com.infopulse.infomail.dto.mail.EmailTemplateDTO;
import com.infopulse.infomail.dto.mail.RecipientDTO;
import com.infopulse.infomail.models.mail.EmailSchedule;
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
@RequestMapping("api/v1/emails")
@AllArgsConstructor
public class EmailController {

	private final CronSchedulerService cronSchedulerService;


	@PostMapping
	public ResponseEntity<EmailDTO> addEmail(@Valid @RequestBody EmailDTO emailDTO, Authentication authentication) {
		try {
			List<RecipientDTO> recipients = emailDTO.getRecipients();
			EmailTemplateDTO emailTemplateDTO = emailDTO.getEmailTemplate();
			EmailSchedule emailSchedule = EmailSchedule.fromDTO(emailDTO.getEmailSchedule());
			String userEmail = (String) authentication.getPrincipal();
			Long userId = (Long) authentication.getCredentials();

			cronSchedulerService.createTask(recipients, emailTemplateDTO, emailSchedule, userEmail, userId);
		} catch (Exception e) {
			return new ResponseEntity<>(emailDTO, HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(emailDTO, HttpStatus.CREATED);
	}

}
