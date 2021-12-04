package com.infopulse.infomail;

import com.infopulse.infomail.models.schedule.EmailSchedule;
import com.infopulse.infomail.repositories.security.AppUserRepository;
import com.infopulse.infomail.services.mail.EmailSenderService;
import com.infopulse.infomail.services.scheduler.SchedulerService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.time.Instant;
import java.util.Date;

@Slf4j
@SpringBootTest()
class InfoMailApplicationTests {
	private final Scheduler scheduler;
	private final SchedulerService<CronTrigger, EmailSchedule> schedulerService;
	private final EmailSenderService mailSender;
	private final AppUserRepository appUserRepository;

	@Autowired
	InfoMailApplicationTests(Scheduler scheduler, SchedulerService<CronTrigger,EmailSchedule> schedulerService, EmailSenderService mailSender, AppUserRepository appUserRepository) {
		this.scheduler = scheduler;
		this.schedulerService = schedulerService;
		this.mailSender = mailSender;
		this.appUserRepository = appUserRepository;
	}

	@Test
	void contextLoads() throws ParseException, SchedulerException, InterruptedException {
//		AppUser id = appUserRepository.save(new AppUser("dsfsdfsd","fsdfsdss", AppUserRole.USER,
//				true,true,true));
//		System.out.println(id.getUserId());
		appUserRepository.findAllEmails().forEach(System.out::println);
//		CronScheduleBuilder shed = (CronScheduleBuilder) schedulerService.buildSchedule("* * * ? * * *");
		JobDetail job = schedulerService.buildJobDetail("lolan", 243L,
				"desc", Action.class);
		Date from = Date.from(Instant.now());
//		CronTrigger cron = (CronTrigger) schedulerService.buildTrigger(job, shed, from, Date.from(Instant.now().plusMillis(2900)), false);
//		scheduler.scheduleJob(job, cron);
//		mailSender.sendSimpleEmails();
//		Thread.currentThread().sleep(7000);
	}

	@Slf4j
	public static class Action implements Job {

		@Override
		public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
			log.info(jobExecutionContext.getJobDetail().getKey().getName());
			log.info((String) jobExecutionContext.getJobDetail().getJobDataMap().get("messageTemplateId"));
		}
	}

}
