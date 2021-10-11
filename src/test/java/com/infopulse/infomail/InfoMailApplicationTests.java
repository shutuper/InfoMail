package com.infopulse.infomail;

import com.infopulse.infomail.services.mail.EmailSenderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest()
class InfoMailApplicationTests {

	public final EmailSenderService mailSender;

	@Autowired
	InfoMailApplicationTests(EmailSenderService mailSender) {
		this.mailSender = mailSender;
	}

	@Test
	void contextLoads() {
		mailSender.sendSimpleEmail("ttatta3adpot@gmail.com", "first message", "Very important subject!");
		mailSender.sendSimpleEmail("ttatta3adpot@gmail.com", "2 message", "Important subject!");
		mailSender.sendSimpleEmail("ttatta3adpot@gmail.com", "3 message", "Not important subject!");
	}

}
