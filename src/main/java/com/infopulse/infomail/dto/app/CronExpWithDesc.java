package com.infopulse.infomail.dto.app;


import lombok.*;
import org.quartz.CronExpression;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CronExpWithDesc {

	private CronExpression cronExpression;
	private String cronDescription;

}
