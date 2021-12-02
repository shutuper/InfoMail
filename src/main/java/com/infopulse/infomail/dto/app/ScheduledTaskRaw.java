package com.infopulse.infomail.dto.app;

public interface ScheduledTaskRaw {

	String getJobName();

	Long getOrderId();

	String getDescription();

	String getTriggerState();

	Long getStartAt();

	Long getEndAt();

	String getSubject();
}
