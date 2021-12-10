package com.infopulse.infomail.services.scheduler;

import com.infopulse.infomail.dto.app.CronExpWithDesc;
import com.infopulse.infomail.dto.api.templates.EmailTemplateDTO;
import com.infopulse.infomail.dto.api.emails.RecipientDTO;
import com.infopulse.infomail.models.mail.AppUserEmailsInfo;
import com.infopulse.infomail.models.schedule.EmailSchedule;
import com.infopulse.infomail.models.templates.EmailTemplate;
import com.infopulse.infomail.models.mail.enums.RepeatType;
import com.infopulse.infomail.models.quartz.QrtzJobDetail;
import com.infopulse.infomail.services.mail.RecipientService;
import com.infopulse.infomail.services.mail.AppUserEmailsInfoService;
import com.infopulse.infomail.services.mail.EmailTemplateService;
import com.infopulse.infomail.services.scheduler.cronGenerator.CronGenerator;
import com.infopulse.infomail.services.scheduler.jobs.EmailSendJob;
import com.infopulse.infomail.services.scheduler.сronDescriptor.CronDescriptorService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class CronSchedulerService implements SchedulerService<CronTrigger, EmailSchedule> {

	private final QrtzJobDetailService qrtzJobDetailService;
	private final AppUserEmailsInfoService appUserEmailsInfoService;
	private final Scheduler scheduler;
	private final RecipientService recipientService;
	private final CronDescriptorService cronDescriptorService;
	private final EmailTemplateService emailTemplateService;

	@PostConstruct
	private void init() {
		try {
			scheduler.start();
			log.info("\n+=+=+=+=+=+=+=+=+=+ Scheduler started +=+=+=+=+=+=+=+=+=+\n");
		} catch (SchedulerException e) {
			log.error(e.getMessage(), e);
		}

	}

	@PreDestroy
	private void preDestroy() {
		try {
			scheduler.shutdown();
			log.info("\n+=+=+=+=+=+=+=+=+=+ Scheduler finished +=+=+=+=+=+=+=+=+=+\n");
		} catch (SchedulerException e) {
			log.error(e.getMessage(), e);
		}
	}

	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public void createTask(List<RecipientDTO> recipients,
	                       EmailTemplateDTO emailTemplateDTO,
	                       EmailSchedule emailSchedule,
	                       String userEmail,
	                       Long userId) throws ParseException, SchedulerException {

		EmailTemplate emailTemplate = emailTemplateService.saveEmailTemplate(
				emailTemplateDTO,
				userId,
				userEmail);

		CronExpWithDesc cronExpWithDesc = generateCronExpressionWithDescription(emailSchedule);

		ScheduleBuilder<CronTrigger> scheduleBuilder = buildSchedule(cronExpWithDesc.getCronExpression());

		JobDetail jobDetail = buildJobDetail(
				userEmail,
				emailTemplate.getId(),
				cronExpWithDesc.getCronDescription(),
				EmailSendJob.class);

		Trigger trigger = buildTrigger(
				jobDetail,
				scheduleBuilder,
				emailSchedule);

		scheduleJob(jobDetail, trigger, recipients, emailTemplate);
	}

	public CronExpWithDesc generateCronExpressionWithDescription(EmailSchedule
			                                                             emailSchedule) throws ParseException {
		RepeatType scheduleRepeat = emailSchedule.getRepeatAt();
		String cronDescription;

		if (Objects.isNull(scheduleRepeat) || scheduleRepeat.equals(RepeatType.NOTHING))
			return new CronExpWithDesc();

		String cronExpression = CronGenerator.generate(emailSchedule);
		CronExpression.validateExpression(cronExpression);

		cronDescription = cronDescriptorService.getDescription(cronExpression);
		log.info("Valid cron expression: {} with description {}", cronExpression, cronDescription);

		return new CronExpWithDesc(new CronExpression(cronExpression), cronDescription);
	}

	@Override
	public ScheduleBuilder<CronTrigger> buildSchedule(CronExpression cronExpression) {
		if (Objects.isNull(cronExpression))
			return null;

		return CronScheduleBuilder.cronSchedule(cronExpression);
	}

	@Override
	public Trigger buildTrigger(JobDetail jobDetail,
	                            ScheduleBuilder<CronTrigger> scheduleBuilder,
	                            EmailSchedule emailSchedule) {
		JobKey jobKey = jobDetail.getKey();
		TriggerKey triggerKey = new TriggerKey(jobKey.getName(), jobKey.getGroup());

		TriggerBuilder<Trigger> builder = TriggerBuilder.newTrigger()
				.withIdentity(triggerKey)
				.forJob(jobKey);

		Date nowDate = Date.from(Instant.now());
		boolean startNow = emailSchedule.isSendNow();

		LocalDateTime sendDateTime = emailSchedule.getSendDateTime();
		if (sendDateTime != null)
			sendDateTime = sendDateTime.minusMinutes(1);


		Date startAt = parseDateFromLocal(sendDateTime);
		Date endAt = parseDateFromLocal(emailSchedule.getEndDate());

		if (startNow) builder.startNow();
		else if (validDateForScheduling(startAt, nowDate, false))
			builder.startAt(startAt);

		if (validDateForScheduling(endAt, startAt, true))
			builder.endAt(endAt);

		if (scheduleBuilder != null)
			builder.withSchedule(scheduleBuilder);

		return builder.build();
	}

	boolean validDateForScheduling(Date date,
	                               Date compared,
	                               boolean canBeNullable) {
		if (canBeNullable && (date == null))
			return false;

		if ((date != null) && date.after(compared))
			return true;

		throw new IllegalArgumentException("Date is not valid for scheduling");
	}

	Date parseDateFromLocal(Temporal temporal) {
		if (temporal == null)
			return null;

		if (temporal instanceof LocalDateTime)
			return Timestamp.valueOf((LocalDateTime) temporal);

		if (temporal instanceof LocalDate)
			return Timestamp.valueOf(((LocalDate) temporal).atStartOfDay().plusDays(1L));

		throw new IllegalArgumentException("Can't parse date: " + temporal);
	}


	@Override
	public JobDetail buildJobDetail(String userEmail,
	                                long messageTemplateId,
	                                String description,
	                                Class<? extends Job> jobClass) {
		String uniqueJobName = UUID.randomUUID().toString(); // generating random unique job name

		return JobBuilder.newJob(jobClass)
				.withIdentity(uniqueJobName, userEmail) // grouping JobDetails by users' emails
				.withDescription(description)   // .usingJobData(jobDataMap)
//				.storeDurably() // whether store jobDetails after all triggers executed
				.build();
	}

	public void scheduleJob(JobDetail jobDetail,
	                        Trigger trigger,
	                        List<RecipientDTO> recipientsDTO,
	                        EmailTemplate emailTemplate) throws SchedulerException {

		JobKey jobKey = jobDetail.getKey();
		scheduler.scheduleJob(jobDetail, trigger);

		QrtzJobDetail qrtzJobDetail = qrtzJobDetailService.findQrtzJobDetailByJobKey(jobKey);
		AppUserEmailsInfo appUserEmailsInfo = appUserEmailsInfoService.saveAppUserEmailsInfo(qrtzJobDetail, emailTemplate);

		recipientService.saveAllRecipientsWithUserInfo(recipientsDTO, appUserEmailsInfo);

		log.info("User's(email = {}) job(name = {}) scheduled: {}",
				jobKey.getGroup(), jobKey.getName(), jobDetail.getDescription());
	}


	@Override
	public ScheduleBuilder<CronTrigger> buildSchedule(boolean alwaysRepeat, int repeatCount, long interval) {
		return null;
	}

}
