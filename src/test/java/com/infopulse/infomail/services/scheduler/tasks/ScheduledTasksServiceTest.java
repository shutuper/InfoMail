package com.infopulse.infomail.services.scheduler.tasks;

import com.infopulse.infomail.dto.api.schedule.ScheduledTaskWithEmailDTO;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ScheduledTasksServiceTest {

    @Autowired
    private ScheduledTasksService tasksService;

    private final String DISABLE_REASON = "Disabled because jobName was obtained from an existing database, " +
            "if you specify the actual 'jobName' and 'jobGroup' values, then you can remove the annotation";
    private final String jobName = "f2f02d80-3d24-49af-96e9-376d43bc5558";
    private final String jobGroup = "admin@infomail.com";

    @Disabled(DISABLE_REASON)
    @Test
    void getTaskDtoByJobName() {
        ScheduledTaskWithEmailDTO dto = tasksService.getTaskDtoByJobName(jobName, jobGroup);

        assertNotNull(dto);
        assertFalse(dto.getRecipients().isEmpty());

        System.out.println("dto = " + dto);
    }
}
