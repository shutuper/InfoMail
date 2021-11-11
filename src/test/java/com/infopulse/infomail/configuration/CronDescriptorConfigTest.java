package com.infopulse.infomail.configuration;

import it.burning.cron.CronExpressionParser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CronDescriptorConfigTest {

    @Autowired
    private CronExpressionParser.Options cronDescriptorOptions;


    @Test
    void whenCronDescriptorOptions() {
        CronExpressionParser.Options exp = new CronExpressionParser.Options() {{
            setLocale("en");
            setVerbose(true);
        }};

        CronExpressionParser.Options actual = this.cronDescriptorOptions;

        assertEquals(exp.getLocale(), actual.getLocale());
        assertEquals(exp.isVerbose(), actual.isVerbose());
    }
}
