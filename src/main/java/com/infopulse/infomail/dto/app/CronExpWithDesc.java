package com.infopulse.infomail.dto.app;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.quartz.CronExpression;

@Getter
@Setter
@AllArgsConstructor
public class CronExpWithDesc {

	private CronExpression cronExpression;
	private String cronDescription;

}
