package com.infopulse.infomail;

import com.infopulse.infomail.models.User;
import com.infopulse.infomail.repositories.UserRepository;
import com.infopulse.infomail.services.mail.EmailSenderService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest()
@Slf4j
class InfoMailApplicationTests {

	public final EmailSenderService mailSender;
	private final UserRepository userRepository;

	@Autowired
	InfoMailApplicationTests(EmailSenderService mailSender, UserRepository userRepository) {
		this.mailSender = mailSender;
		this.userRepository = userRepository;
	}

	@Test
	void contextLoads() {
		mailSender.sendSimpleEmail("ttatta3adpot@gmail.com", "first message", "Very important subject!");
		mailSender.sendSimpleEmail("ttatta3adpot@gmail.com", "2 message", "Important subject!");
		mailSender.sendSimpleEmail("ttatta3adpot@gmail.com", "3 message", "Not important subject!");
		userRepository.findAll().stream().map(User::toString).forEach(log::info);
	}

}
