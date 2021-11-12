package com.infopulse.infomail.controllers.rest;

import com.infopulse.infomail.dto.app.CronExpWithDesc;
import com.infopulse.infomail.dto.mail.EmailDTO;
import com.infopulse.infomail.dto.mail.EmailTemplateDTO;
import com.infopulse.infomail.dto.mail.RecipientDTO;
import com.infopulse.infomail.dto.mail.EmailSchedule;
import com.infopulse.infomail.models.mail.EmailTemplate;
import com.infopulse.infomail.services.mail.EmailTemplateService;
import com.infopulse.infomail.services.scheduler.CronSchedulerService;
import com.infopulse.infomail.services.scheduler.jobs.EmailSendJob;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.ScheduleBuilder;
import org.quartz.Trigger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
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
	private final EmailTemplateService emailTemplateService;


	@PostMapping
	public ResponseEntity<EmailDTO> addEmail(@Valid @RequestBody EmailDTO emailDTO, Authentication authentication) {
		try {
			List<RecipientDTO> recipients = emailDTO.getRecipients();
			EmailTemplateDTO emailTemplateDTO = emailDTO.getEmailTemplate();
			EmailSchedule emailSchedule = emailDTO.getEmailSchedule();
			String userEmail = (String) authentication.getPrincipal();
			Long userId = (Long) authentication.getCredentials();

			EmailTemplate emailTemplate = emailTemplateService.saveEmailTemplate(emailTemplateDTO, userId);

			CronExpWithDesc cronExpWithDesc = cronSchedulerService
					.generateCronExpressionWithDescription(emailSchedule);

			ScheduleBuilder<CronTrigger> scheduleBuilder = cronSchedulerService
					.buildSchedule(cronExpWithDesc.getCronExpression());

			JobDetail jobDetail = cronSchedulerService.buildJobDetail(
					userEmail,
					emailTemplate.getId(),
					// it should be replaced
					cronExpWithDesc.getCronDescription(),
					EmailSendJob.class);

			Trigger trigger = cronSchedulerService.buildTrigger(
					jobDetail,
					scheduleBuilder,
					emailSchedule);

			cronSchedulerService.scheduleJob(jobDetail, trigger, recipients, emailTemplate);
		} catch (Exception e) {
			return new ResponseEntity<>(emailDTO, HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(emailDTO, HttpStatus.CREATED);
	}

}