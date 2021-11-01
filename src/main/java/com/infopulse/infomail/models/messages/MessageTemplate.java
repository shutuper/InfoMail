package com.infopulse.infomail.models.messages;

import com.infopulse.infomail.models.users.AppUser;
import lombok.*;

import javax.persistence.*;


@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MessageTemplate {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long messageId;

	@ToString.Exclude
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "owner_user_Id")
	private AppUser owner;

	@Column(nullable = false)
	private String subject;

	@Column(columnDefinition = "text", nullable = false)
	private String body;

	@Column(nullable = false)
	private String sharingLink;

//	@Column(nullable = false)
//	private LocalDateTime sentDate;

}