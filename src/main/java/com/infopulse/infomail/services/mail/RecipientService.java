package com.infopulse.infomail.services.mail;

import com.infopulse.infomail.dto.api.emails.RecipientDTO;
import com.infopulse.infomail.models.mail.AppUserEmailsInfo;
import com.infopulse.infomail.models.mail.enums.RecipientType;
import com.infopulse.infomail.models.recipients.AppRecipient;
import com.infopulse.infomail.repositories.AppRecipientRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.*;

@Service
@AllArgsConstructor
public class RecipientService {

	private final AppRecipientRepository recipientRepository;


	public List<RecipientDTO> getAllAsDTOByJobName(String jobName) {
		List<AppRecipient> appRecipients = recipientRepository.findAllByUserInfo_QrtzJobDetail_JobName(jobName);
		if (appRecipients.isEmpty())
			throw new IllegalStateException(String.format("There are no recipients with QrtzJobDetail_JobName: %s", jobName));
		return appRecipients.stream()
				.map(x -> new RecipientDTO(x.getEmail(), x.getRecipientType())).toList();
	}

	public List<AppRecipient> getAllByUserInfoId(Long userInfoId) {
		List<AppRecipient> recipients = recipientRepository.findAllByUserInfo_Id(userInfoId);
		if (recipients.isEmpty())
			throw new IllegalStateException(String.format("There are no recipients with userInfoId: %s", userInfoId));

		return recipients;
	}


	public List<RecipientDTO> getAllAsDTOByUserInfoId(Long userInfo) {
		return getAllByUserInfoId(userInfo).stream()
				.map(x -> new RecipientDTO(x.getEmail(), x.getRecipientType())).toList();
	}


	public Map<RecipientType, List<String>> groupRecipients(List<AppRecipient> recipients) {
		return recipients.stream()
				.collect(
						groupingBy(
								AppRecipient::getRecipientType,
								mapping(AppRecipient::getEmail, toList())
						));
	}


	public void saveAllRecipientsWithUserInfo(List<RecipientDTO> recipientsDTO,
	                                          AppUserEmailsInfo appUserEmailsInfo) {
		recipientRepository.saveAll(
				recipientsDTO.stream()
						.map(x -> new AppRecipient(
								x.getEmail(),
								x.getRecipientType(),
								appUserEmailsInfo))
						.collect(toList())
		);
	}

}
