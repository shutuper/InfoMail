package com.infopulse.infomail.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "confirmation_token")
public class ConfirmationToken {


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long tokenId;
	@Column(nullable = false)
	private String token;
	@Column(nullable = false)
	private LocalDateTime createdAt;
	@Column(nullable = false)
	private LocalDateTime expiredAt;
	private LocalDateTime confirmedAt;
	@ManyToOne
	@JoinColumn(name = "app_user_id", nullable = false, unique = true)
	private AppUser appUser;

	public ConfirmationToken(String token, LocalDateTime createdAt, LocalDateTime expiredAt, AppUser appUser) {
		this.token = token;
		this.createdAt = createdAt;
		this.expiredAt = expiredAt;
		this.appUser = appUser;
	}

}