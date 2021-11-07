package com.infopulse.infomail.services;

import com.infopulse.infomail.models.users.AppUser;
import com.infopulse.infomail.models.confirmation.ConfirmationToken;
import com.infopulse.infomail.repositories.AppUserRepository;
import com.infopulse.infomail.services.registration.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class AppUserService {

	//private final static String USER_NOT_FOUND = "user with email %s not found";
	private final AppUserRepository appUserRepository;
	private final PasswordEncoder passwordEncoder;
	private final ConfirmationTokenService confirmationTokenService;

	@Transactional
	public String singUp(AppUser appUser) throws IllegalStateException {
		Optional<AppUser> optOldUser = appUserRepository
				.findAppUserByEmail(appUser.getEmail());

		// One token per 30 minutes
		String token = getValidConfirmTokenOrNewOne(appUser, optOldUser) // UUID.randomUUID().toString();
				.orElseThrow(() -> new IllegalStateException("User already has valid token"));

		log.info("User {} signed up", appUser.getEmail());
		return token;
	}

	public void enableAppUser(String email) {
		appUserRepository.enableAppUserByEmail(email);
		log.info("User {}'s is no enabled", email);
	}

	public void deleteUnConfirmedAppUser(Long appUserId) {
		appUserRepository.deleteAppUserByUserId(appUserId);
	}


	//Next methods are utils -------------------------------------------------------------------------------------------------------

	private Optional<String> getValidConfirmTokenOrNewOne(AppUser appUser, Optional<AppUser> optOldUser) throws IllegalStateException {
		if (optOldUser.isPresent()) {
			AppUser oldUser = optOldUser.get();

			if (oldUser.isEnabled()) // appUser's entered email already confirmed
				throw new IllegalStateException(String.format("Email %s already exists!", appUser.getEmail()));

			Optional<ConfirmationToken> confirmationToken = confirmationTokenService
					.getConfirmationTokenByAppUserId(oldUser.getUserId());

			if (confirmationToken.isPresent()) {
				if (tokenIsExpired(confirmationToken.get())) {

					log.info("Deleting {}'s expired confirmation token", oldUser.getEmail());

					confirmationTokenService.deleteConfirmationTokenById(confirmationToken.get().getTokenId());
					confirmationTokenService.flushRepository();
				} else return Optional.empty();

				log.info("Generating new confirmation token for {}", oldUser.getEmail());

				return getNewSavedToken(oldUser);
			}
		}

		String encodedPassword = passwordEncoder.encode(appUser.getPassword()); // encoding user password
		appUser.setPassword(encodedPassword);

		log.info("Saving new user {} to database", appUser.getEmail());
		AppUser newUser = appUserRepository.save(appUser); // saving new user and acquiring it with new ID

		return getNewSavedToken(newUser);
	}

	private Optional<String> getNewSavedToken(AppUser appUser) {
		String token = UUID.randomUUID().toString();
		saveNewConfirmationToken(appUser, token); // saving new token
		return Optional.of(token);
	}

	private boolean tokenIsExpired(ConfirmationToken token) {
		return LocalDateTime.now().isAfter(token.getExpiredAt());
	}

	private void saveNewConfirmationToken(AppUser appUser, String token) {
		ConfirmationToken confirmationToken = new ConfirmationToken(
				token,
				LocalDateTime.now(),
				LocalDateTime.now().plusMinutes(30),
				appUser
		);
		log.info("Saving {}'s new confirmation token to database", appUser.getEmail());

		confirmationTokenService.saveConfirmationToken(confirmationToken);
	}
}
