package com.infopulse.infomail.models.mail.enums;

public enum EmailStatus {

	SENT, // successfully sent email

	WAIT, // waiting for trigger

	FAILED // failed because of server problems
}
