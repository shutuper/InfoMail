package com.infopulse.infomail.services.mail;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.util.List;


@Slf4j
@Service
public class EmailSenderService {

	@Value("${application.email.sentFrom}")
	private String sendFrom;
	private final JavaMailSender mailSender;

	public EmailSenderService(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	@Async
	public void sendSimpleEmail(String to, String body, String subject) throws IllegalStateException {
		try {

			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom(sendFrom);

			message.setText(body);
			message.setSubject(subject);

			message.setTo(to);
			mailSender.send(message);
			log.info("Email send to {}, subject: {}", to, subject);
		} catch (Exception e) {
			log.error("Failed sending email to {}", to, e);
			throw new IllegalStateException("failed to send email");
		}
	}



}
