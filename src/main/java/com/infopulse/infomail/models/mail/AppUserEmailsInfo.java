package com.infopulse.infomail.models.mail;

import com.infopulse.infomail.models.mail.EmailTemplate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppUserEmailsInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String jobName;

	private String jobGroup;

	@ManyToOne(fetch = FetchType.LAZY)
	private EmailTemplate emailTemplate;

	public AppUserEmailsInfo(String jobName, String jobGroup, EmailTemplate emailTemplate) {
		this.jobName = jobName;
		this.jobGroup = jobGroup;
		this.emailTemplate = emailTemplate;
	}
}
