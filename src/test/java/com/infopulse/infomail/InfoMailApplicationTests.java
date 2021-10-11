package com.infopulse.infomail;

import com.infopulse.infomail.configuration.InfoMailConfiguration;
import com.infopulse.infomail.services.mail.EmailSenderService;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

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
