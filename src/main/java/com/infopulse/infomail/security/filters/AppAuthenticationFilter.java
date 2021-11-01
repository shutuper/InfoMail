package com.infopulse.infomail.security.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infopulse.infomail.dto.EmailPasswordRequest;
import com.infopulse.infomail.security.jwt.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.infopulse.infomail.security.SecurityConstants.*;

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
			EmailPasswordRequest authRequest = new ObjectMapper()
					.readValue(request.getInputStream(), EmailPasswordRequest.class);

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
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
		String access_token = jwtUtil.createAccessToken(request, authentication); // Map<String, String> tokens = new HashMap<>();
		response.setHeader(AUTHORIZATION_HEADER, TOKEN_PREFIX + access_token); // tokens.put(AUTHORIZATION_HEADER, access_token);
	}

}


/*		String refresh_token = JWT.create()
				.withSubject(user.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis() + 30 * 60 * 1000))
				.withIssuer(request.getRequestURL().toString())
				.sign(algorithm);
        response.setHeader("access_token", access_token);
        response.setHeader("refresh_token", refresh_token);
        tokens.put("refresh_token", refresh_token);
        */