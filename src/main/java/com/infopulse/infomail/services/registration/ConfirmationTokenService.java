package com.infopulse.infomail.services.registration;

import com.infopulse.infomail.models.confirmation.ConfirmationToken;
import com.infopulse.infomail.repositories.ConfirmationTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {

	private final ConfirmationTokenRepository confirmationTokenRepository;

	@Transactional
	public void saveConfirmationToken(ConfirmationToken token) {
		confirmationTokenRepository.save(token);
	}

	public Optional<ConfirmationToken> getConfirmationByToken(String token) {
		return confirmationTokenRepository.findByToken(token);
	}

	public void setConfirmedAt(String token) {
		confirmationTokenRepository
				.updateConfirmedAt(token, LocalDateTime.now());
	}

	public Optional<ConfirmationToken> getConfirmationTokenByAppUserId(Long appUserId) {
		return confirmationTokenRepository.findByAppUserUserId(appUserId);
	}

	public void deleteConfirmationTokenById(Long tokenId) {
		confirmationTokenRepository.deleteConfirmationTokenByTokenId(tokenId);
	}

	public void deleteConfirmationTokenByAppUserId(Long appUserId) {
		confirmationTokenRepository.deleteConfirmationTokenByAppUserUserId(appUserId);
	}

	public void flushRepository() {
		confirmationTokenRepository.flush();
	}
}
