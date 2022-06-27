package com.infopulse.infomail.controllers.rest;

import com.infopulse.infomail.dto.api.emails.EmailDTO;
import com.infopulse.infomail.dto.api.emails.RecipientDTO;
import com.infopulse.infomail.dto.api.templates.EmailTemplateDTO;
import com.infopulse.infomail.models.schedule.EmailSchedule;
import com.infopulse.infomail.services.scheduler.CronSchedulerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@SecurityRequirement(name = "Authorization")
@RequestMapping("api/v1/emails")
@AllArgsConstructor
public class EmailController {

	private final CronSchedulerService cronSchedulerService;

	@PostMapping
	@Operation(summary = "Permissions: authenticated user",
			description = "Send email template to multiply users by schedule schedule")
	@ApiResponse(
			responseCode = "201", description = "Email scheduled",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmailDTO.class))
	)
	@ApiResponse(
			responseCode = "400", description = "Bad request",
			content = @Content
	)
	public ResponseEntity<EmailDTO> createEmailJob(@Valid @RequestBody EmailDTO emailDTO,
	                                               Authentication authentication) {
		List<RecipientDTO> recipients = emailDTO.getRecipients();
		EmailTemplateDTO emailTemplateDTO = emailDTO.getEmailTemplate();
		EmailSchedule emailSchedule = EmailSchedule.fromDTO(emailDTO.getEmailSchedule());
		String userEmail = (String) authentication.getPrincipal();
		Long userId = (Long) authentication.getCredentials();

		cronSchedulerService.createTask(recipients, emailTemplateDTO, emailSchedule, userEmail, userId);
		return new ResponseEntity<>(emailDTO, HttpStatus.CREATED);
	}

}
