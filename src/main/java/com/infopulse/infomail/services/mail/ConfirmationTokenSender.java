package com.infopulse.infomail.services.mail;

public interface ConfirmationTokenSender {

	void sendConfirmationToken(String to, String token);

}
