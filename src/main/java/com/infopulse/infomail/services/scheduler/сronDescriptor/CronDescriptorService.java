package com.infopulse.infomail.services.scheduler.сronDescriptor;

import it.burning.cron.CronExpressionDescriptor;
import it.burning.cron.CronExpressionParser;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CronDescriptorService {


	private CronExpressionParser.Options cronDescriptorOptions;

	public String getDescription(String cronExpression) {
		return CronExpressionDescriptor.getDescription(cronExpression, cronDescriptorOptions);
	}

	// For others languages
	public String getDescription(String cronExpression, CronExpressionParser.Options options) {
		return CronExpressionDescriptor.getDescription(cronExpression, options);
	}

}
