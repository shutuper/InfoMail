package com.infopulse.infomail.security.filters;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.infopulse.infomail.security.jwt.JwtUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import static com.infopulse.infomail.security.config.SecurityConstants.*;
import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@Slf4j
@Component
@AllArgsConstructor
public class AppAuthorizationFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request,
	                                HttpServletResponse response,
	                                FilterChain filterChain) throws ServletException, IOException {

		String authorizationHeader = request.getHeader(AUTHORIZATION);

		if (isNullOrEmpty(authorizationHeader) || !authorizationHeader.startsWith(TOKEN_PREFIX)) {
			filterChain.doFilter(request, response);
			return;
		}

		try {
			DecodedJWT decodedJWT = jwtUtil.verifyAccessToken(authorizationHeader);

			String email = decodedJWT.getSubject();
			String[] roles = decodedJWT.getClaim(ROLES_CLAIM).asArray(String.class);
			Long userId = decodedJWT.getClaim(USER_ID_CLAIM).asLong();
			Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

			stream(roles).forEach(role ->
					authorities.add(new SimpleGrantedAuthority(role)));

			UsernamePasswordAuthenticationToken authenticationToken =
					new UsernamePasswordAuthenticationToken(
							email,
							userId,
							authorities
					);
			SecurityContextHolder.getContext().setAuthentication(authenticationToken);

			log.info("User {} token verified", email);

		} catch (Exception exception) {
			log.error("Error during authorizing: {}", exception.getMessage());

			response.setStatus(FORBIDDEN.value());
			response.setHeader("error", exception.getMessage());

//          response.sendError(FORBIDDEN.value());
//			Map<String, String> error = new HashMap<>();
//			error.put("error_message", exception.getMessage());
//			response.setContentType(APPLICATION_JSON_VALUE);
//			new ObjectMapper().writeValue(response.getOutputStream(), error);
//			return;
		}
		// TODO: frontend should handle errors and redirect user to auth-page
		filterChain.doFilter(request, response);
	}

	private boolean isNullOrEmpty(String str) {
		return str == null || str.isEmpty();
	}

	private boolean isAuthenticationRequested(HttpServletRequest request) {
		return request.getServletPath().equals(AUTHENTICATION_URL);
	}
}




