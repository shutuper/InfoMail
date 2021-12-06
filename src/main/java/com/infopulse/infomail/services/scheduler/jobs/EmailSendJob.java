package com.infopulse.infomail.services.scheduler.jobs;

import com.infopulse.infomail.models.mail.AppUserEmailsInfo;
import com.infopulse.infomail.models.templates.EmailTemplate;
import com.infopulse.infomail.models.mail.enums.RecipientType;
import com.infopulse.infomail.services.mail.AppUserEmailsInfoService;
import com.infopulse.infomail.services.mail.RecipientService;
import com.infopulse.infomail.services.mail.EmailSenderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@AllArgsConstructor
public class EmailSendJob extends QuartzJobBean {

	private final RecipientService recipientService;
	private final EmailSenderService emailSenderService;
	private final AppUserEmailsInfoService appUserEmailsInfoService;

	@Override
	protected void executeInternal(JobExecutionContext context) {
		JobDetail jobDetail = context.getJobDetail();
		String jobName = jobDetail.getKey().getName();
		String senderEmail = jobDetail.getKey().getGroup();

		AppUserEmailsInfo appUserEmailsInfo = appUserEmailsInfoService
				.getAppUserEmailsInfoByJobName(jobName);

		Map<RecipientType, List<String>> groupedRecipients = recipientService
				.groupRecipients(recipientService.getAllByUserInfoId(appUserEmailsInfo.getId()));

		EmailTemplate emailTemplate = appUserEmailsInfo.getEmailTemplate();
		emailSenderService.sendScheduledMimeEmail(emailTemplate, groupedRecipients, appUserEmailsInfo, senderEmail);
	}
}
