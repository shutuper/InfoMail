package com.infopulse.infomail.services.mail;

import com.infopulse.infomail.models.mail.AppUserEmailsInfo;
import com.infopulse.infomail.models.mail.EmailTemplate;
import com.infopulse.infomail.models.quartz.QrtzJobDetail;
import com.infopulse.infomail.repositories.AppUserEmailsInfoRepository;
import lombok.AllArgsConstructor;
import org.quartz.JobKey;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AppUserEmailsInfoService {

	private final AppUserEmailsInfoRepository appUserEmailsInfoRepository;

	public AppUserEmailsInfo saveAppUserEmailsInfo(QrtzJobDetail jobDetail, EmailTemplate emailTemplate) {
		AppUserEmailsInfo appUserEmailsInfo = new AppUserEmailsInfo(
				jobDetail,
				emailTemplate);

		return appUserEmailsInfoRepository.save(appUserEmailsInfo);
	}

	public AppUserEmailsInfo getAppUserEmailsInfoByJobName(String jobName) {
		return appUserEmailsInfoRepository.findByQrtzJobDetail_JobName(jobName).orElseThrow(
				() -> new IllegalStateException(
						String.format("AppUserEmailsInfo with jobName %s does not exist", jobName)
				)
		);
	}


}
