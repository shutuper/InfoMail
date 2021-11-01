package com.infopulse.infomail.services.scheduler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.text.ParseException;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class CronSchedulerService implements SchedulerService<CronTrigger> {

	private final static String messageTemplateIdProp = "messageTemplateId";

	private final Scheduler scheduler;


	@Override
	public ScheduleBuilder<CronTrigger> buildSchedule(String schedule) throws ParseException {
		CronExpression.validateExpression(schedule);
		log.info("Successful validation: {}", schedule);

		return CronScheduleBuilder.cronSchedule(schedule);
	}

	@Override
	public Trigger buildTrigger(JobDetail jobDetail,
	                            ScheduleBuilder<CronTrigger> scheduleBuilder,
	                            Date startAt,
	                            Date endAt) {

		JobKey jobKey = jobDetail.getKey();
		TriggerKey triggerKey = new TriggerKey(jobKey.getName(), jobKey.getGroup());

		return TriggerBuilder.newTrigger()
				.withIdentity(triggerKey)
				.withSchedule(scheduleBuilder)
				.forJob(jobKey)
				.startAt(startAt)
				.endAt(endAt)
				.build();
	}

	@Override
	public JobDetail buildJobDetail(String userEmail,
	                                long messageTemplateId,
	                                String description,
	                                Class<? extends Job> jobClass) {

		JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.put(messageTemplateIdProp, String.valueOf(messageTemplateId));

		String uniqueJobName = UUID.randomUUID().toString(); // generating random job name

		return JobBuilder.newJob(jobClass)
				.withIdentity(uniqueJobName, userEmail) // grouping JobDetails by users' emails
				.withDescription(description)
				.usingJobData(jobDataMap)
				.storeDurably() // whether store jobDetails after all triggers executed
				.build();
	}

	@PostConstruct
	private void init() {
		try {
			scheduler.start();
			log.info("Scheduler started successfully");
		} catch (SchedulerException e) {
			log.error(e.getMessage(), e);
		}

	}

	@PreDestroy
	private void preDestroy() {
		try {
			scheduler.shutdown();
			log.info("Scheduler finished");
		} catch (SchedulerException e) {
			log.error(e.getMessage(), e);
		}
	}

	@Override
	public ScheduleBuilder<CronTrigger> buildSchedule(boolean alwaysRepeat,
	                                                  int repeatCount,
	                                                  long interval) throws ParseException {
		return null;
	}
}
