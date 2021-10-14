package com.infopulse.infomail.models;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.Objects;

@Entity
@Table(name = "messages_to")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class MessageTo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "message_to_id", nullable = false)
	private Long messageToID;
	@Email
	@Column(nullable = false)
	private String recipientMail;
	@ManyToOne
	@JoinColumn(name = "message_id", nullable = false)
	private Message message;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		MessageTo messageTo = (MessageTo) o;
		return Objects.equals(messageToID, messageTo.messageToID);
	}

	@Override
	public int hashCode() {
		return 0;
	}
}