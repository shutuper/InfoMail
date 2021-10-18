package com.infopulse.infomail;

import com.infopulse.infomail.models.AppUser;
import com.infopulse.infomail.models.AppUserRole;
import com.infopulse.infomail.repositories.AppUserRepository;
import com.infopulse.infomail.services.mail.EmailSenderService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest()
class InfoMailApplicationTests {

	public final EmailSenderService mailSender;
	private final AppUserRepository appUserRepository;

	@Autowired
	InfoMailApplicationTests(EmailSenderService mailSender, AppUserRepository appUserRepository) {
		this.mailSender = mailSender;
		this.appUserRepository = appUserRepository;
	}

	@Test
	void contextLoads() {
		AppUser id = appUserRepository.save(new AppUser("dsfsdfsd","fsdfsdss", AppUserRole.USER,
				true,true,true));
		System.out.println(id.getUserId());
		mailSender.sendSimpleEmail("ttattsdfsdfsdfsdft@gmail.com", "first message", "Very important subject!");
//		mailSender.sendSimpleEmail("ttatta3adpot@gmail.com", "2 message", "Important subject!");
//		mailSender.sendSimpleEmail("ttatta3adpot@gmail.com", "3 message", "Not important subject!");
		appUserRepository.findAll().stream().map(AppUser::toString).forEach(log::info);
	}

}
