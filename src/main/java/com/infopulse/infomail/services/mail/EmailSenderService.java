package com.infopulse.infomail.services.mail;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service("EmailSenderServiceTest")
@AllArgsConstructor
public class EmailSenderService {

	private final String sendFrom;
	private final JavaMailSender mailSender;
	private final Logger log = LoggerFactory.getLogger(EmailSenderService.class);

	public void sendSimpleEmail(String to, String body, String subject) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(sendFrom);
		message.setTo(to);
		message.setText(body);
		message.setSubject(subject);
		mailSender.send(message);
		log.info("Email send to {}, subject: {}", to, subject);
	}

}
