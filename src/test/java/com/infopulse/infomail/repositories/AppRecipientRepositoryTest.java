package com.infopulse.infomail.repositories;

import com.infopulse.infomail.models.mail.AppRecipient;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class AppRecipientRepositoryTest {

    @Autowired
    private AppRecipientRepository recipientRepository;

    private final String DISABLE_REASON = "Disabled because jobName was obtained from an existing database, " +
            "if you specify the actual 'jobName' value, then you can remove the annotation";
    private final String JOB_NAME = "d77641db-913d-4e74-bf48-6f82ab660ae5";

    @Disabled(DISABLE_REASON)
    @Test
    void findAllByUserInfo_QrtzJobDetail_JobName_thenNotEmptyList() {
        List<AppRecipient> recipients = recipientRepository.findAllByUserInfo_QrtzJobDetail_JobName(JOB_NAME);
        assertFalse(recipients.isEmpty());
    }

    @Test
    void findAllByUserInfo_QrtzJobDetail_JobName_thenEmptyList() {
        List<AppRecipient> recipients = recipientRepository.findAllByUserInfo_QrtzJobDetail_JobName("not found name");
        assertTrue(recipients.isEmpty());
    }
}
