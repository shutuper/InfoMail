package com.infopulse.infomail.services.scheduler.jobs;

import com.infopulse.infomail.dto.mail.EmailDTO;
import com.infopulse.infomail.dto.mail.EmailSchedule;
import com.infopulse.infomail.dto.mail.EmailTemplateDTO;
import com.infopulse.infomail.dto.mail.RecipientDTO;
import com.infopulse.infomail.models.mail.EmailTemplate;
import com.infopulse.infomail.models.mail.enums.RecipientType;
import com.infopulse.infomail.models.mail.enums.RepeatType;
import com.infopulse.infomail.services.AppUserService;
import com.infopulse.infomail.services.mail.EmailTemplateService;
import com.infopulse.infomail.services.scheduler.CronSchedulerService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
		UsernamePasswordAuthenticationToken token =
				new UsernamePasswordAuthenticationToken(AppUserService.ADMIN_EMAIL,
						AppUserService.ADMIN_ID.toString());
		Authentication authentication = token;

		EmailDTO emailDTO = getRandEmailDTO();
		List<RecipientDTO> recipients = emailDTO.getRecipients();
		EmailTemplateDTO emailTemplateDTO = emailDTO.getEmailTemplate();
		String userEmail = (String) authentication.getPrincipal();
		Long userId = Long.parseLong((String) authentication.getCredentials());
		try {
			EmailTemplate emailTemplate = emailTemplateService.saveEmailTemplate(emailTemplateDTO, userId);
			Long emailTemplateId = emailTemplate.getId();

			JobDetail jobDetail = cronSchedulerService.buildJobDetail(
					userEmail,
					emailTemplateId,
					// it should be replaced
					description,
					EmailSendJob.class);

			Trigger trigger = TriggerBuilder.newTrigger()
					.withIdentity(UUID.randomUUID().toString())
					.forJob(jobDetail)
					.startNow()
//					.withSchedule(CronScheduleBuilder
//							.cronSchedule("0/10 * * ? * * *"))
					.endAt(Timestamp.valueOf(LocalDateTime.now().plusSeconds(30)))
					.build();

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

		emailDTO.setEmailSchedule(emailSchedule.toDTO());

		List<RecipientDTO> recipients = new ArrayList<>(List.of(
				new RecipientDTO("ttatta3adpot@gmail.com", RecipientType.CC),
				new RecipientDTO("egor-xt@ukr.net", RecipientType.TO),
				new RecipientDTO("fsdfsdf@ukr.net", RecipientType.TO)
		));

		emailDTO.setRecipients(recipients);

		EmailTemplateDTO emailTemplateDTO = new EmailTemplateDTO();
		emailTemplateDTO.setBody("Tempalte body");
		emailTemplateDTO.setSubject("Subject - lol");

		emailDTO.setEmailTemplate(emailTemplateDTO);
		return emailDTO;
	}

}
