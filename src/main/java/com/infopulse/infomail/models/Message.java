package com.infopulse.infomail.models;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Message {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long messageId;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "app_user_Id")
	private AppUser appUser;
	@Column(nullable = false)
	private String subject;
	@Column(columnDefinition = "text")
	private String messageBody;
	@Column(nullable = false)
	private LocalDateTime sentDate;

}