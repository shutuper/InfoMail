package com.infopulse.infomail.services.mail;

import com.infopulse.infomail.models.mail.AppUserEmailsInfo;
import com.infopulse.infomail.models.mail.EmailLog;
import com.infopulse.infomail.models.templates.EmailTemplate;
import com.infopulse.infomail.models.mail.enums.EmailStatus;
import com.infopulse.infomail.models.mail.enums.RecipientType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

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
	private final PlatformTransactionManager transactionManager;

	public EmailSenderService(JavaMailSender mailSender, EmailLogService emailLogService,
	                          PlatformTransactionManager transactionManager) {
		this.mailSender = mailSender;
		this.emailLogService = emailLogService;
		this.transactionManager = transactionManager;
	}

	public void sendScheduledMimeEmail(EmailTemplate email,
	                                   Map<RecipientType, List<String>> groupedRecipients,
	                                   AppUserEmailsInfo appUserEmailsInfo, String senderEmail) {

		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);

		try {

			execute(email, groupedRecipients);
			emailLogService.saveNewEmailLog(appUserEmailsInfo, null, EmailStatus.SENT, senderEmail);

			transactionManager.commit(status);
			log.info("Job with user's info id: {} executed", appUserEmailsInfo.getId());
		} catch (Exception exception) {
			String expMessage = exception.getMessage();

			log.error(String.format("Job with user's info id: %s failed", appUserEmailsInfo.getId()),
					exception);
			transactionManager.rollback(status);
			emailLogService.saveNewEmailLog(
					appUserEmailsInfo, expMessage,
					EmailStatus.FAILED, senderEmail);
		}
	}

	public EmailLog resendFailedMimeEmail(EmailTemplate email,
	                                  Map<RecipientType, List<String>> groupedRecipients, EmailLog emailLog) {
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);
		EmailLog result;
		try {
			execute(email, groupedRecipients);
			result = emailLogService.saveSentEmailLog(emailLog);

			transactionManager.commit(status);
			log.info("Email with id {} is successfully resent", emailLog.getId());
		} catch (Exception exception) {
			String expMessage = emailLogService.getValidExceptionMessage(exception.getMessage());

			log.error(String.format("Sending email with id %s is failed again", emailLog.getId()),
					exception);
			transactionManager.rollback(status);
			result = emailLogService.saveFailedEmailLog(emailLog, expMessage);
		}
		return result;
	}

	private void execute(EmailTemplate email, Map<RecipientType, List<String>> groupedRecipients) throws MessagingException {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

		helper.setFrom(sendFrom);
		helper.setText(email.getBody(), true);
		helper.setSubject(email.getSubject());
		setGroupedRecipientsToEmail(groupedRecipients, helper); // set TO, CC, BCC recipients

		mailSender.send(mimeMessage); // sending email template to all recipients
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