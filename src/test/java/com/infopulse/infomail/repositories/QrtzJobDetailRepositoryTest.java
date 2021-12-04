package com.infopulse.infomail.repositories;

import com.infopulse.infomail.dto.api.ScheduledTaskFullDTO;
import com.infopulse.infomail.dto.app.ScheduledTaskFullRaw;
import com.infopulse.infomail.models.quartz.QrtzJobDetail;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class QrtzJobDetailRepositoryTest {

    @Autowired private QrtzJobDetailRepository qrtzJobDetailRepository;

    private final String DISABLE_REASON = "Disabled because jobName was obtained from an existing database, " +
            "if you specify the actual 'jobName' value, then you can remove the annotation";
    private final String jobName = "d77641db-913d-4e74-bf48-6f82ab660ae5";

    @Disabled(DISABLE_REASON)
    @Test
    void getDTOByJobName_thenIsPresent() {
        Optional<ScheduledTaskFullRaw> dtoByJobName = qrtzJobDetailRepository.getDTOByJobName(jobName);

        assertTrue(dtoByJobName.isPresent());
        ScheduledTaskFullDTO scheduledTaskFullDTO = new ScheduledTaskFullDTO(dtoByJobName.get(), null);
        System.out.println("scheduledTaskFullDTO = " + scheduledTaskFullDTO);

    }

    @Test
    void getDTOByJobName_thenNotPresent() {
        Optional<ScheduledTaskFullRaw> dtoByJobName = qrtzJobDetailRepository.getDTOByJobName("not found name");

        assertFalse(dtoByJobName.isPresent());
    }

    @Disabled(DISABLE_REASON)
    @Test
    void findByJobName() {
        Optional<QrtzJobDetail> jobDetail = qrtzJobDetailRepository.findByJobName(jobName);

        assertTrue(jobDetail.isPresent());

    }
}
