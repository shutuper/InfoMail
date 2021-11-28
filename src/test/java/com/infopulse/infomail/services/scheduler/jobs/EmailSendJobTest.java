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

import java.sql.Timestamp;
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
			EmailSchedule emailSchedule = EmailSchedule.fromDTO(emailDTO.getEmailSchedule());
			String userEmail = (String) authentication.getPrincipal();
			Long userId = (Long) authentication.getCredentials();

			EmailTemplate emailTemplate = emailTemplateService.saveEmailTemplate(emailTemplateDTO, userId, userEmail);

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
			for (int i = 0; i < 20; i++) {
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
		emailSchedule.setEndDate(LocalDate.now().plusDays(1));
		emailDTO.setEmailSchedule(emailSchedule.toDTO());

		List<RecipientDTO> recipients = new ArrayList<>(List.of(
				new RecipientDTO("fsdf412sdf@ukr.net", RecipientType.TO),
				new RecipientDTO("fsdf12sdf@ukr.net", RecipientType.TO),
				new RecipientDTO("fsdfsdf@ukr.net", RecipientType.TO),
				new RecipientDTO("fsdfs423df@ukr.net", RecipientType.TO),
				new RecipientDTO("fsdf14sdf@ukr.net", RecipientType.TO),
				new RecipientDTO("fsd23fsdf@ukr.net", RecipientType.TO),
				new RecipientDTO("fsdfsdf@ukr.net", RecipientType.TO),
				new RecipientDTO("fsdfs5df@ukr.net", RecipientType.TO),
				new RecipientDTO("fsdfsdf@ukr.net", RecipientType.TO),
				new RecipientDTO("f4sdfsdf@ukr.net", RecipientType.TO),
				new RecipientDTO("fsdfs3df@ukr.net", RecipientType.TO),
				new RecipientDTO("fsdfsdf@ukr.net", RecipientType.TO),
				new RecipientDTO("f1sdfsdf@ukr.net", RecipientType.TO),
				new RecipientDTO("fsdfsdf@ukr.net", RecipientType.TO),
				new RecipientDTO("fsdfs2df@ukr.net", RecipientType.TO),
				new RecipientDTO("fsdfs2sdf@ukr.net", RecipientType.TO),
				new RecipientDTO("fsdfs21ddf@ukr.net", RecipientType.TO),
				new RecipientDTO("fsdfs2faddf@ukr.net", RecipientType.TO),
				new RecipientDTO("fsdfs2adfsdf@ukr.net", RecipientType.TO),
				new RecipientDTO("fsdfs2afdsdf@ukr.net", RecipientType.TO),
				new RecipientDTO("fsdf34sdf@ukr.net", RecipientType.TO),
				new RecipientDTO("fsdf2sdf@ukr.net", RecipientType.TO),
				new RecipientDTO("fsdf3sdf@ukr.net", RecipientType.TO),
				new RecipientDTO("fsdfsdf@ukr.net", RecipientType.TO),
				new RecipientDTO("fasasf@ukr.net", RecipientType.BCC),
				new RecipientDTO("fsdfasfsdf@ukr.net", RecipientType.CC)
		));

		emailDTO.setRecipients(recipients);

		EmailTemplateDTO emailTemplateDTO = new EmailTemplateDTO();
		emailTemplateDTO.setBody("BODYBODYBODY");
		emailTemplateDTO.setSubject("SUBJECTSUBJECTSUBJECT");

		emailDTO.setEmailTemplate(emailTemplateDTO);
		return emailDTO;
	}

}
