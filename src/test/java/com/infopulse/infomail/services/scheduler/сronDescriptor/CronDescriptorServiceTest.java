package com.infopulse.infomail.services.scheduler.сronDescriptor;

import it.burning.cron.CronExpressionParser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CronDescriptorServiceTest {

    private final CronDescriptorService descriptorService;

    @Autowired
    CronDescriptorServiceTest(CronDescriptorService descriptorService) {
        this.descriptorService = descriptorService;
    }


    @Test
    void whenGetDescription() {
        String actual = descriptorService.getDescription("0 0 12 * * ?");
        String exp = "At 12:00, every day";

        assertEquals(exp, actual);
    }

    @Test
    void whenGetDescription_butWithOptions() {
        CronExpressionParser.Options options = new CronExpressionParser.Options() {{
            setVerbose(true);
            setLocale("uk");
        }};

        String actual = descriptorService.getDescription("0 0 12 * * ?", options);
        String exp = "О 12:00, щоденно";

        assertEquals(exp, actual);
    }
}
