package com.infopulse.infomail.models.mail;

import com.infopulse.infomail.models.Template;
import com.infopulse.infomail.models.users.AppUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;


@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class UserEmailTemplate implements Template {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ToString.Exclude
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "app_user_id")
	private AppUser appUser;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String subject;

	@Column(columnDefinition = "text", nullable = false)
	private String body;

	@Column(nullable = false, unique = true)
	private String sharingLink;

	public UserEmailTemplate(AppUser appUser, String name, String subject, String body, String sharingLink) {
		this.appUser = appUser;
		this.name = name;
		this.subject = subject;
		this.body = body;
		this.sharingLink = sharingLink;
	}
}
