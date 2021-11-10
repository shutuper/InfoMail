package com.infopulse.infomail.services.mail;

import com.infopulse.infomail.models.mail.AppUserEmailsInfo;
import com.infopulse.infomail.models.mail.EmailTemplate;
import com.infopulse.infomail.models.mail.enums.EmailStatus;
import com.infopulse.infomail.models.mail.enums.RecipientType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;


@Slf4j
@Service
public class EmailSenderService {

	@Value("${application.email.sentFrom}")
	private String sendFrom;

	private final JavaMailSender mailSender;
	private final EmailLogService emailLogService;

	public EmailSenderService(JavaMailSender mailSender, EmailLogService emailLogService) {
		this.mailSender = mailSender;
		this.emailLogService = emailLogService;
	}

	public void sendMimeEmail(EmailTemplate email,
	                          Map<RecipientType, List<String>> groupedRecipients,
	                          AppUserEmailsInfo appUserEmailsInfo) {

		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

		try {

			helper.setFrom(sendFrom);
			helper.setText(email.getBody(), true);
			helper.setSubject(email.getSubject());
			setGroupedRecipientsToEmail(groupedRecipients, helper); // set TO, CC, BCC recipients

			mailSender.send(mimeMessage); // sending email template to all recipients

			emailLogService.saveEmailLog(appUserEmailsInfo, null, EmailStatus.SENT);
			log.info("Job with user's info id: {} executed", appUserEmailsInfo.getId());
		} catch (MessagingException | IllegalStateException e) {
			String expMessage = e.getMessage();
			log.error(expMessage, e);
			emailLogService.saveEmailLog(appUserEmailsInfo, expMessage, EmailStatus.FAILED);
		}
	}

	private void setGroupedRecipientsToEmail(
			Map<RecipientType, List<String>> groupedRecipients,
			MimeMessageHelper helper) throws MessagingException, IllegalStateException {

		for (RecipientType type : groupedRecipients.keySet()) {

			String[] recipientsByGroup = groupedRecipients
					.get(type)
					.toArray(String[]::new);

			switch (type) {
				case TO -> helper.setTo(recipientsByGroup);
				case CC -> helper.setCc(recipientsByGroup);
				case BCC -> helper.setBcc(recipientsByGroup);
				default -> throw new IllegalStateException(
						MessageFormat.format("Recipient type: {1} does not exist", type)
				);
			}
		}
	}

}

//	@Async
//	public void sendSimpleEmail(String to, String body, String subject) throws IllegalStateException {
//		try {
//
//			SimpleMailMessage message = new SimpleMailMessage();
//			message.setFrom(sendFrom);
//
//			message.setText(body);
//			message.setSubject(subject);
//
//			message.setTo(to);
//			mailSender.send(message);
//			log.info("Email send to {}, subject: {}", to, subject);
//		} catch (Exception e) {
//			log.error("Failed sending email to {}", to, e);
//			throw new IllegalStateException("failed to send email");
//		}
//	}