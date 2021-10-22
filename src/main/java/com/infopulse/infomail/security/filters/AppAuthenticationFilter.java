package com.infopulse.infomail.security.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infopulse.infomail.models.AppUser;
import com.infopulse.infomail.models.requestBodies.EmailPasswordRequest;
import com.infopulse.infomail.security.SecurityConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.infopulse.infomail.security.SecurityConstants.*;


@Slf4j
public class AppAuthenticationFilter extends UsernamePasswordAuthenticationFilter {


	private final AuthenticationManager authenticationManager;

	public AppAuthenticationFilter(AuthenticationManager authenticationManager) {
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
					new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword());

			return authenticationManager.authenticate(authenticationToken);
		} catch (IOException e) {
			throw new IllegalStateException("Unsuccessful authentication");
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
		String access_token = createAccessToken(request, authentication);

		Map<String, String> tokens = new HashMap<>();
		tokens.put(AUTHORIZATION_HEADER, access_token);

		response.setHeader(AUTHORIZATION_HEADER, TOKEN_PREFIX + access_token);
	}

	private String createAccessToken(HttpServletRequest request, Authentication authentication) {
		AppUser user = (AppUser) authentication.getPrincipal();
		Algorithm algorithm = Algorithm.HMAC256(SECRET.getBytes());
		return JWT.create()
				.withSubject(user.getUsername()) // actually email!
				.withIssuedAt(new Date(System.currentTimeMillis()))
				.withExpiresAt(new Date(System.currentTimeMillis() + TOKEN_LIFETIME))
				.withIssuer(request.getRequestURL().toString())
				.withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
				.sign(algorithm);
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