package com.infopulse.infomail.services.registration;

import com.infopulse.infomail.models.AppUser;
import com.infopulse.infomail.models.AppUserRole;
import com.infopulse.infomail.models.ConfirmationToken;
import com.infopulse.infomail.models.requestBodies.RegistrationRequest;
import com.infopulse.infomail.services.AppUserService;
import com.infopulse.infomail.services.mail.ConfirmationTokenSender;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RegistrationService {

	private final AppUserService appUserService;
	private ConfirmationTokenService confirmationTokenService;
	private final ConfirmationTokenSender confirmationTokenSender;

	public String register(RegistrationRequest request) {
		String token = appUserService.singUp(new AppUser(
				request.getEmail(),
				request.getPassword(),
				AppUserRole.USER,
				true, true, false));
		confirmationTokenSender.sendConfirmationToken(request.getEmail(), token);
		return token;
	}

	@Transactional
	public String confirmToken(String token) {
		ConfirmationToken confirmationToken = validateConfirmationToken(token);
		confirmationTokenService.setConfirmedAt(token);
		appUserService.enableAppUser(confirmationToken.getAppUser().getEmail());
		return "confirmed";
	}

	private ConfirmationToken validateConfirmationToken(String token) {
		ConfirmationToken confirmationToken = confirmationTokenService
				.getConfirmationByToken(token).orElseThrow(() -> new IllegalStateException("token not found!"));

		if (confirmationToken.getConfirmedAt() != null)
			throw new IllegalStateException("email already confirmed!");

		if (confirmationToken.getAppUser().isEnabled())
			throw new IllegalStateException("Can't reject token, because user is already confirmed!");

		if (confirmationToken.getExpiredAt().isBefore(LocalDateTime.now()))
			throw new IllegalStateException("token expired!");

		return confirmationToken;
	}

	@Transactional
	public String rejectToken(String token) {
		ConfirmationToken confirmationToken = validateConfirmationToken(token);
		AppUser currentUser = confirmationToken.getAppUser();
		String userEmail = currentUser.getEmail();
		confirmationTokenService.deleteConfirmationTokenById(confirmationToken.getTokenId());
		appUserService.deleteUnConfirmedAppUser(currentUser.getUserId());
		return String.format("Unconfirmed account: %s is successfully deleted!", userEmail);
	}
}
