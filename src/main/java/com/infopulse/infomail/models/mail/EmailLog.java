package com.infopulse.infomail.models.mail;

import com.infopulse.infomail.models.mail.enums.EmailStatus;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class EmailLog {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	@Column(columnDefinition = "text")
	private String message; // for causes of fails

	@Column(nullable = false)
	private LocalDateTime logDateTime; // when it was inserted in database

	@Enumerated(EnumType.STRING)
	private EmailStatus emailStatus;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_info_id")
	private AppUserEmailsInfo userInfo;

	@Email
	@Column(nullable = false)
	private String senderEmail;

	public EmailLog(String message,
	                LocalDateTime logDateTime,
	                EmailStatus emailStatus,
	                AppUserEmailsInfo userInfo,
	                String senderEmail) {
		this.message = message;
		this.logDateTime = logDateTime;
		this.emailStatus = emailStatus;
		this.userInfo = userInfo;
		this.senderEmail = senderEmail;
	}

}
