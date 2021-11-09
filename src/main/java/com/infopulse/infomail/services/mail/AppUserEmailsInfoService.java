package com.infopulse.infomail.services.mail;

import com.infopulse.infomail.models.mail.AppUserEmailsInfo;
import com.infopulse.infomail.models.mail.EmailTemplate;
import com.infopulse.infomail.repositories.AppUserEmailsInfoRepository;
import lombok.AllArgsConstructor;
import org.quartz.JobKey;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AppUserEmailsInfoService {

	private final AppUserEmailsInfoRepository appUserEmailsInfoRepository;

	public AppUserEmailsInfo saveAppUserEmailsInfo(JobKey jobKey, EmailTemplate emailTemplate) {
		AppUserEmailsInfo appUserEmailsInfo = new AppUserEmailsInfo(
				jobKey.getName(),
				jobKey.getGroup(),
				emailTemplate);

		return appUserEmailsInfoRepository.saveAndFlush(appUserEmailsInfo);
	}

	public AppUserEmailsInfo getAppUserEmailsInfoByJobName(String jobName) {
		return appUserEmailsInfoRepository.findByJobName(jobName).orElseThrow(
				() -> new IllegalStateException(
						String.format("AppUserEmailsInfo with jobName %s does not exist", jobName)
				)
		);
	}


}
