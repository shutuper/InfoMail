package com.infopulse.infomail.services.scheduler;

import com.infopulse.infomail.services.QrtzJobDetailService;
import com.infopulse.infomail.services.RecipientService;
import com.infopulse.infomail.services.mail.AppUserEmailsInfoService;
import com.infopulse.infomail.services.mail.EmailTemplateService;
import com.infopulse.infomail.services.scheduler.сronDescriptor.CronDescriptorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.quartz.Scheduler;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class CronSchedulerServiceTest {

    @InjectMocks
    private CronSchedulerService schedulerService;

    @Mock
    private QrtzJobDetailService qrtzJobDetailService;
    private AppUserEmailsInfoService appUserEmailsInfoService;
    private Scheduler scheduler;
    private RecipientService recipientService;
    private CronDescriptorService cronDescriptorService;
    private EmailTemplateService emailTemplateService;


    @Test
    void validDateForScheduling_thenFalse() {
        boolean result = schedulerService.validDateForScheduling(null, null, true);
        assertFalse(result);
    }

    @Test
    void validDateForScheduling_thenTrue() {
        Date date = Timestamp.valueOf(LocalDateTime.now());
        Date compared = Timestamp.valueOf(LocalDateTime.now().minusDays(1));
        boolean result = schedulerService.validDateForScheduling(date, compared, true);
        assertTrue(result);
    }

    @Test
    void validDateForScheduling_thenIllegalArgumentException() {
        Date compared = Timestamp.valueOf(LocalDateTime.now().minusDays(1));
        assertThrows(IllegalArgumentException.class,
                () -> schedulerService.validDateForScheduling(null, compared, false));
    }

    @Test
    void parseDateFromLocal_thenNUll() {
        Date date = schedulerService.parseDateFromLocal(null);
        assertNull(date);
    }

    @Test
    void parseDateFromLocal_butInstanceOfLocalDateTime_thenDate() {
        LocalDateTime temporal = LocalDateTime.now();
        Date exp = Timestamp.valueOf(temporal);
        Date actual = schedulerService.parseDateFromLocal(temporal);
        assertEquals(exp, actual);
    }

    @Test
    void parseDateFromLocal_butInstanceOfLocalDate_thenDate() {
        LocalDate temporal = LocalDate.now();
        Date exp = Timestamp.valueOf(temporal.atStartOfDay().plusDays(1L));
        Date actual = schedulerService.parseDateFromLocal(temporal);
        assertEquals(exp, actual);
    }
}
