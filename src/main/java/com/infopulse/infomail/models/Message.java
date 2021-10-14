package com.infopulse.infomail.models;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "messages")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Message {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "message_id", nullable = false)
	private Long messageId;

	@ManyToOne()
	@JoinColumn(name = "user_id", nullable = false)
	private User author;
	private String subject;
	@Column(columnDefinition = "text", nullable = false)
	private String messageBody;
	private LocalDateTime sentDate;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		Message message = (Message) o;
		return Objects.equals(messageId, message.messageId);
	}

	@Override
	public int hashCode() {
		return 0;
	}
}