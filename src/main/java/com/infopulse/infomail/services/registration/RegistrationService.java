package com.infopulse.infomail.services.registration;

import com.infopulse.infomail.dto.exeptions.MessageDTO;
import com.infopulse.infomail.dto.securityRequests.RegistrationRequest;
import com.infopulse.infomail.models.confirmation.ConfirmationToken;
import com.infopulse.infomail.models.users.AppUser;
import com.infopulse.infomail.models.users.roles.AppUserRole;
import com.infopulse.infomail.services.security.AppUserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@AllArgsConstructor
public class RegistrationService {

	private final AppUserService appUserService;
	private ConfirmationTokenService confirmationTokenService;
	private final ConfirmationTokenSender confirmationTokenSender;

	public MessageDTO register(RegistrationRequest request) {
		try {
			String token = appUserService.singUp(
					new AppUser(
							request.getEmail(),
							request.getPassword(),
							AppUserRole.USER,
							true, true, false));

			confirmationTokenSender.sendConfirmationToken(request.getEmail(), token);

			String success = "success";
			return new MessageDTO(success);
		} catch (IllegalStateException ex) {
			log.error(ex.getMessage());

			return new MessageDTO(ex.getMessage());
		}
	}

	@Transactional
	public MessageDTO confirmToken(String token) {
		String result, email;
		ConfirmationToken confirmationToken;
		try {
			confirmationToken = validateConfirmationToken(token);

			confirmationTokenService.setConfirmedAt(token);
			email = confirmationToken.getAppUser().getEmail();
			appUserService.enableAppUser(email);

			log.info("User {} have just confirmed email", email);

			result = "Confirmed";
		} catch (IllegalStateException ex) {
			log.error(ex.getMessage());
			result = ex.getMessage();
		}
		return new MessageDTO(result);
	}

	private ConfirmationToken validateConfirmationToken(String token) throws IllegalStateException {
		ConfirmationToken confirmationToken = confirmationTokenService
				.getConfirmationByToken(token).orElseThrow(() -> new IllegalStateException("token not found!"));

		if (confirmationToken.getConfirmedAt() != null)
			throw new IllegalStateException("Email already confirmed!");

		if (confirmationToken.getAppUser().isEnabled())
			throw new IllegalStateException("Can't reject token, because user is already confirmed!");

		if (confirmationToken.getExpiredAt().isBefore(LocalDateTime.now()))
			throw new IllegalStateException("Current token is expired!");

		return confirmationToken;
	}

	@Transactional
	public MessageDTO rejectToken(String token) {
		ConfirmationToken confirmationToken = validateConfirmationToken(token);
		AppUser currentUser = confirmationToken.getAppUser();
		String userEmail = currentUser.getEmail();
		confirmationTokenService.deleteConfirmationTokenById(confirmationToken.getTokenId());
		appUserService.deleteUnConfirmedAppUser(currentUser.getUserId());
		return new MessageDTO(String.format("Unconfirmed account: %s is successfully deleted!", userEmail));
	}
}
