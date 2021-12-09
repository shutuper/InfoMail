package com.infopulse.infomail.services.scheduler.сronDescriptor;

import it.burning.cron.CronExpressionParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CronDescriptorConfig {

    @Bean
    public CronExpressionParser.Options cronDescriptorOptions() {
        return new CronExpressionParser.Options() {{
            setLocale("en");    // language translation
            setVerbose(true);   // write a full description
        }};
    }

}
