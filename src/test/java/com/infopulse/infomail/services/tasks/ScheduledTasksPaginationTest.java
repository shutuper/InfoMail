package com.infopulse.infomail.services.tasks;

import com.infopulse.infomail.services.scheduler.tasks.ScheduledTasksService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ScheduledTasksPaginationTest {

	@Autowired
	private ScheduledTasksService scheduledTasksService;

	@Test
	public void scheduledTasksPaginationTest() {
		scheduledTasksService
				.getUserPaginatedTasks(0, 15, 1, "end_time", "admin@infomail.com")
				.getTasks()
				.forEach(System.out::println);
		scheduledTasksService
				.getUserPaginatedTasks(0, 15, 1, "start_time", "admin@infomail.com")
				.getTasks()
				.forEach(System.out::println);
		scheduledTasksService
				.getUserPaginatedTasks(0, 15, 1, "order_id", "admin@infomail.com")
				.getTasks()
				.forEach(System.out::println);

	}

}
