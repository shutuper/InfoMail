package com.infopulse.infomail.services.scheduler.jobs;

import com.infopulse.infomail.dto.app.CronExpWithDesc;
import com.infopulse.infomail.dto.mail.EmailDTO;
import com.infopulse.infomail.dto.mail.EmailTemplateDTO;
import com.infopulse.infomail.dto.mail.RecipientDTO;
import com.infopulse.infomail.models.mail.EmailSchedule;
import com.infopulse.infomail.models.mail.EmailTemplate;
import com.infopulse.infomail.models.mail.enums.RecipientType;
import com.infopulse.infomail.models.mail.enums.RepeatType;
import com.infopulse.infomail.services.AppUserService;
import com.infopulse.infomail.services.mail.EmailTemplateService;
import com.infopulse.infomail.services.scheduler.CronSchedulerService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@SpringBootTest()
public class EmailSendJobTest {
	@Autowired
	private CronSchedulerService cronSchedulerService;
	@Autowired
	private EmailTemplateService emailTemplateService;

	// TODO: create cron expression descriptor and add desc to jobDetails
	private static final String description = "FUTURE DESC OF CRON EXPRESSION";

	@Test
	public void testAddEmail() {
		Authentication authentication =
				new UsernamePasswordAuthenticationToken(
						AppUserService.ADMIN_EMAIL,
						AppUserService.ADMIN_ID);
		EmailDTO emailDTO = getRandEmailDTO();
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
					CronScheduleBuilder.cronSchedule("* * * * * ? *"),
					emailSchedule);

			cronSchedulerService.scheduleJob(jobDetail, trigger, recipients, emailTemplate);
			for (int i = 0; i < 40; i++) {
				Thread.sleep(1000);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		log.info("succeeded!========");
	}

	private EmailDTO getRandEmailDTO() {
		EmailDTO emailDTO = new EmailDTO();

		EmailSchedule emailSchedule = new EmailSchedule();
		emailSchedule.setRepeatAt(RepeatType.NOTHING);
		emailSchedule.setSendDateTime(LocalDateTime.now().plusSeconds(3));
		emailSchedule.setSendNow(true);
		emailDTO.setEmailSchedule(emailSchedule);
		emailSchedule.setEndDate(LocalDate.now().plusDays(1));

		List<RecipientDTO> recipients = new ArrayList<>(List.of(
//				new RecipientDTO("ludagnitiyy@gmail.com", RecipientType.CC),
				new RecipientDTO("egor-xt@ukr.net", RecipientType.TO)
//				new RecipientDTO("fsdfsdf@ukr.net", RecipientType.TO)
		));

		emailDTO.setRecipients(recipients);

		EmailTemplateDTO emailTemplateDTO = new EmailTemplateDTO();
		emailTemplateDTO.setBody("BODYBODYBODY");
		emailTemplateDTO.setSubject("SUBJECTSUBJECTSUBJECT");

		emailDTO.setEmailTemplate(emailTemplateDTO);
		return emailDTO;
	}

}
