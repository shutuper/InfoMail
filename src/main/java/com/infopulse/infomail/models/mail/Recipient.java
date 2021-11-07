package com.infopulse.infomail.models.mail;

import com.infopulse.infomail.models.mail.enums.RecipientType;

public interface Recipient {

	RecipientType getRecipientType();

	String getEmail();
}
