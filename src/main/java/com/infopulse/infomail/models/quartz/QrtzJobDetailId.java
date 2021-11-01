package com.infopulse.infomail.models.quartz;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
@EqualsAndHashCode
public class QrtzJobDetailId implements Serializable {

	private static final long serialVersionUID = -7077383962343514735L;

	@Column(name = "sched_name", nullable = false, length = 120)
	private String schedName;
	@Column(name = "job_name", nullable = false, length = 200)
	private String jobName;
	@Column(name = "job_group", nullable = false, length = 200)
	private String jobGroup;

}