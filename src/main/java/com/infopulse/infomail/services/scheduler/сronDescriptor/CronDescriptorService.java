package com.infopulse.infomail.services.scheduler.сronDescriptor;

import it.burning.cron.CronExpressionDescriptor;
import it.burning.cron.CronExpressionParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CronDescriptorService {

    @Autowired
    private CronExpressionParser.Options cronDescriptorOptions;

    public String getDescription(String cronExpression) {
        return CronExpressionDescriptor.getDescription(cronExpression, cronDescriptorOptions);
    }

    public String getDescription(String cronExpression, CronExpressionParser.Options options) {
        return CronExpressionDescriptor.getDescription(cronExpression, options);
    }

}
