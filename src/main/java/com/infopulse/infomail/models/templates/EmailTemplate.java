package com.infopulse.infomail.models.templates;

import com.infopulse.infomail.dto.api.templates.EmailTemplateDTO;
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
public class EmailTemplate implements Template {

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


	public EmailTemplate(AppUser appUser, String subject, String body) {
		this.appUser = appUser;
		this.subject = subject;
		this.body = body;
	}

	public EmailTemplate(UserEmailTemplate userEmailTemplate) {
		this.appUser = userEmailTemplate.getAppUser();
		this.body = userEmailTemplate.getBody();
		this.subject = userEmailTemplate.getSubject();
	}

	public EmailTemplateDTO toDto() {
		return new EmailTemplateDTO(
				this.getId(),
				this.getSubject(),
				this.getBody()
		);
	}
}
