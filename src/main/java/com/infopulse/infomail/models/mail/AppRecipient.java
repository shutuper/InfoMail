package com.infopulse.infomail.models.mail;

import com.infopulse.infomail.models.mail.enums.RecipientType;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AppRecipient implements Recipient {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Email
	@Column(nullable = false)
	private String email;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private RecipientType recipientType;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_info_id")
	private AppUserEmailsInfo userInfo;

	public AppRecipient(String email, RecipientType recipientType, AppUserEmailsInfo userInfo) {
		this.email = email;
		this.recipientType = recipientType;
		this.userInfo = userInfo;
	}


}