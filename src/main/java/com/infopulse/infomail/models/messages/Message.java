package com.infopulse.infomail.models.messages;

import com.infopulse.infomail.models.quartz.QrtzJobDetail;
import com.infopulse.infomail.models.quartz.QrtzJobDetailId;
import lombok.*;
import org.quartz.JobDetail;

import javax.persistence.*;
import javax.validation.constraints.Email;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Message {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "message_id")
	private Long messageId;

	@Email
	@Column(nullable = false)
	private String recipientEmail;

	@ToString.Exclude
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns(
			{
					@JoinColumn(name = "job_name_ref", referencedColumnName = "job_name"),
					@JoinColumn(name = "job_group_ref", referencedColumnName = "job_group"),
					@JoinColumn(name = "job_sched_name_ref", referencedColumnName = "sched_name")
			})
	private QrtzJobDetail jobDetail;

}