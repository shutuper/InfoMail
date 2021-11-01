package com.infopulse.infomail.services.scheduler;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.ScheduleBuilder;
import org.quartz.Trigger;

import java.text.ParseException;
import java.util.Date;

public interface SchedulerService<T extends Trigger> {

	ScheduleBuilder<T> buildSchedule(String schedule) throws ParseException;

	ScheduleBuilder<T> buildSchedule(boolean alwaysRepeat,
	                                 int repeatCount,
	                                 long interval) throws ParseException; // for simple triggers

	Trigger buildTrigger(JobDetail jobDetail,
	                     ScheduleBuilder<T> scheduleBuilder,
	                     Date startAt,
	                     Date endAt);

	JobDetail buildJobDetail(String userEmail,
	                         long messageTemplateId,
	                         String description,
	                         Class<? extends Job> jobClass);

}
