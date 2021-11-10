package com.infopulse.infomail.models.mail;

import com.infopulse.infomail.models.quartz.QrtzJobDetail;
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
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	@OneToOne(optional = false)
	@JoinColumn(name = "qrtz_job_detail_id")
	private QrtzJobDetail qrtzJobDetail;

//	@Column(nullable = false, unique = true, updatable = false)
//	private String jobName;
//
//	@Column(nullable = false)
//	private String jobGroup;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "email_template_id")
	private EmailTemplate emailTemplate;

	public AppUserEmailsInfo(QrtzJobDetail qrtzJobDetail, EmailTemplate emailTemplate) {
		this.qrtzJobDetail = qrtzJobDetail;

		this.emailTemplate = emailTemplate;
	}
}
