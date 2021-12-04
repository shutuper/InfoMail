package com.infopulse.infomail.services.registration;

public interface ConfirmationTokenSender {

	void sendConfirmationToken(String to, String token);

}
