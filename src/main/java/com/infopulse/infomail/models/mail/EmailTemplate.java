package com.infopulse.infomail.models.mail;

import com.infopulse.infomail.models.users.AppUser;
import lombok.*;

import javax.persistence.*;


@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class EmailTemplate {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ToString.Exclude
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "app_user_id")
	private AppUser appUser;

	@Column(nullable = false)
	private String subject;

	@Column(columnDefinition = "text", nullable = false)
	private String body;

	@Column(nullable = false, unique = true)
	private String sharingLink;

	public EmailTemplate(AppUser appUser, String subject, String body, String sharingLink) {
		this.appUser = appUser;
		this.subject = subject;
		this.body = body;
		this.sharingLink = sharingLink;
	}
}