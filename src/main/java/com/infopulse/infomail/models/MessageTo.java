package com.infopulse.infomail.models;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MessageTo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "message_to_id", nullable = false)
	private Long messageToID;
	@Email
	@Column(nullable = false)
	private String recipientEmail;
	@ManyToOne
	@JoinColumn(name = "message_id", nullable = false)
	private Message message;

}