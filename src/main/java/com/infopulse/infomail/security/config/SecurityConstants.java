package com.infopulse.infomail.security.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public final class SecurityConstants {

	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String AUTHORIZATION_HEADER = "Authorization";
	public static final String EMAIL_PARAM_NAME = "email";
	public static final String PASSWORD_PARAM_NAME = "password";
	public static final String AUTHENTICATION_URL = "/api/v1/authenticate";
	public static final String ACCESS_TOKEN_HEADER = "access_token";
	public static final String ROLES_CLAIM = "roles";
	public static final String USER_ID_CLAIM = "userId";
	public static final int TOKEN_LIFETIME = 60 * 60 * 1000; // 60 minutes

	public static String SECRET;

	@Value("${application.security.secret}")
	public void setSecret(String secret) {
		SecurityConstants.SECRET = secret;
	}

}
