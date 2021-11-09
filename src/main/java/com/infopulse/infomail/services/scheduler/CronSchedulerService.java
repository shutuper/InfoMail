package com.infopulse.infomail.services.scheduler;

import com.infopulse.infomail.dto.mail.RecipientDTO;
import com.infopulse.infomail.models.mail.AppUserEmailsInfo;
import com.infopulse.infomail.models.mail.EmailSchedule;
import com.infopulse.infomail.models.mail.EmailTemplate;
import com.infopulse.infomail.models.mail.enums.RepeatType;
import com.infopulse.infomail.services.RecipientService;
import com.infopulse.infomail.services.mail.AppUserEmailsInfoService;
import com.infopulse.infomail.services.scheduler.cronGenerator.CronGenerator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Service;
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

	public final static String messageTemplateIdProp = "emailTemplateId";


	private final AppUserEmailsInfoService appUserEmailsInfoService;
	private final Scheduler scheduler;
	private final RecipientService recipientService;

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

	@Override
	public ScheduleBuilder<CronTrigger> buildSchedule(EmailSchedule emailSchedule) throws ParseException {
		RepeatType scheduleRepeat = emailSchedule.getRepeatAt();

		if (Objects.isNull(scheduleRepeat) || scheduleRepeat.equals(RepeatType.NOTHING))
			return null;

		String cronExpression = CronGenerator.generate(emailSchedule);
		CronExpression.validateExpression(cronExpression);
		log.info("Valid cron expression: {}", cronExpression);

		return CronScheduleBuilder.cronSchedule(cronExpression);
	}

	@Override
	public Trigger buildTrigger(JobDetail jobDetail,
	                            ScheduleBuilder<CronTrigger> scheduleBuilder,
	                            EmailSchedule emailSchedule) {

		JobKey jobKey = jobDetail.getKey();
		TriggerKey triggerKey = new TriggerKey(jobKey.getName(), jobKey.getGroup());
		TriggerBuilder<Trigger> builder = TriggerBuilder.newTrigger().withIdentity(triggerKey).forJob(jobKey);
		Date nowDate = Date.from(Instant.now());
		boolean startNow = emailSchedule.isSendNow();

		Date startAt = parseDateFromLocal(emailSchedule.getSendDateTime());
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


	@Override
	public JobDetail buildJobDetail(String userEmail,
	                                long messageTemplateId,
	                                String description,
	                                Class<? extends Job> jobClass) {
//		JobDataMap jobDataMap = new JobDataMap();
//		jobDataMap.put(messageTemplateIdProp, String.valueOf(messageTemplateId));

		String uniqueJobName = UUID.randomUUID().toString(); // generating random job name

		return JobBuilder.newJob(jobClass)
				.withIdentity(uniqueJobName, userEmail) // grouping JobDetails by users' emails
				.withDescription(description)   // .usingJobData(jobDataMap)
				.storeDurably() // whether store jobDetails after all triggers executed
				.build();
	}


	@Transactional
	public void scheduleJob(JobDetail jobDetail,
	                        Trigger trigger,
	                        List<RecipientDTO> recipientsDTO,
	                        EmailTemplate emailTemplate) throws SchedulerException {

		JobKey jobKey = jobDetail.getKey();

		scheduler.scheduleJob(jobDetail, trigger);

		AppUserEmailsInfo appUserEmailsInfo = appUserEmailsInfoService.saveAppUserEmailsInfo(jobKey, emailTemplate);
		recipientService.saveAllRecipientsWithUserInfo(recipientsDTO, appUserEmailsInfo);

		log.info("User's(email = {}) job(name = {}) scheduled: {}",
				jobKey.getGroup(), jobKey.getName(), jobDetail.getDescription());
	}


	@Override
	public ScheduleBuilder<CronTrigger> buildSchedule(boolean alwaysRepeat, int repeatCount, long interval) {
		return null;
	}

	private boolean validDateForScheduling(Date date, Date compared, boolean canBeNullable) {
		if (canBeNullable && (date == null))
			return false;
		else if ((date != null) && date.after(compared))
			return true;
		else
			throw new IllegalArgumentException("Date is not valid for scheduling");
	}

	private Date parseDateFromLocal(Temporal temporal) {
		if (temporal == null)
			return null;
		else if (temporal instanceof LocalDate)
			return Timestamp.valueOf(((LocalDate) temporal).atStartOfDay());
		else if (temporal instanceof LocalDateTime)
			return Timestamp.valueOf((LocalDateTime) temporal);
		else throw new IllegalArgumentException("Date can't be parsed");
	}

}
