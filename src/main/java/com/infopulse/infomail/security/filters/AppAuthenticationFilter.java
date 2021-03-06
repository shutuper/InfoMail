package com.infopulse.infomail.security.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infopulse.infomail.dto.securityRequests.AuthenticationRequest;
import com.infopulse.infomail.security.jwt.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.infopulse.infomail.security.config.SecurityConstants.*;

@Slf4j
public class AppAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final JwtUtil jwtUtil;
	private final AuthenticationManager authenticationManager;

	public AppAuthenticationFilter(JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
		this.jwtUtil = jwtUtil;
		this.authenticationManager = authenticationManager;
		setFilterProcessesUrl(AUTHENTICATION_URL);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		try {
			AuthenticationRequest authRequest = new ObjectMapper()
					.readValue(request.getInputStream(), AuthenticationRequest.class);

			log.info("Auth: email is: {}. Password is {}", authRequest.getEmail(), authRequest.getPassword());

			UsernamePasswordAuthenticationToken authenticationToken =
					new UsernamePasswordAuthenticationToken(
							authRequest.getEmail(),
							authRequest.getPassword()
					);

			return authenticationManager.authenticate(authenticationToken);

		} catch (IOException e) {
			log.error(e.getMessage());
			response.setStatus(HttpStatus.BAD_REQUEST.value());
			throw new IllegalStateException("Unsuccessful authentication");
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request,
	                                        HttpServletResponse response,
	                                        FilterChain chain,
	                                        Authentication authentication) {

		String access_token = jwtUtil.createAccessToken(request, authentication);
		response.setHeader(AUTHORIZATION_HEADER, TOKEN_PREFIX.concat(access_token));
	}

}