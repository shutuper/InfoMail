package com.infopulse.infomail.security.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infopulse.infomail.security.SecurityConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;
import static com.infopulse.infomail.security.SecurityConstants.*;

@Slf4j
public class AppAuthorizationFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String authorizationHeader = request.getHeader(AUTHORIZATION);

		if (isNullOrEmpty(authorizationHeader) || !authorizationHeader.startsWith(TOKEN_PREFIX) || isAuthenticationRequested(request)) {
			filterChain.doFilter(request, response);
			return;
		}

		try {

			String token = authorizationHeader.substring(TOKEN_PREFIX.length());

			Algorithm algorithm = Algorithm.HMAC256(SECRET.getBytes());
			JWTVerifier verifier = JWT.require(algorithm).build();
			DecodedJWT decodedJWT = verifier.verify(token);

			String email = decodedJWT.getSubject();
			String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
			Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

			stream(roles).forEach(role ->
					authorities.add(new SimpleGrantedAuthority(role)));

			UsernamePasswordAuthenticationToken authenticationToken =
					new UsernamePasswordAuthenticationToken(email, null, authorities);

			SecurityContextHolder.getContext().setAuthentication(authenticationToken);

		} catch (Exception exception) {
			// TODO: replace all !
			log.error("Error logging in: {}", exception.getMessage());
			response.setHeader("error", exception.getMessage());
			response.setStatus(FORBIDDEN.value());
			//response.sendError(FORBIDDEN.value());
			Map<String, String> error = new HashMap<>();
			error.put("error_message", exception.getMessage());
			response.setContentType(APPLICATION_JSON_VALUE);
			new ObjectMapper().writeValue(response.getOutputStream(), error);
			return;
		}

		filterChain.doFilter(request, response);
	}

	private boolean isNullOrEmpty(String str) {
		return str == null || str.isEmpty();
	}

	private boolean isAuthenticationRequested(HttpServletRequest request) {
		return request.getServletPath().equals(AUTHENTICATION_URL);
	}
}




