package com.infopulse.infomail.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.infopulse.infomail.models.users.AppUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.stream.Collectors;

import static com.infopulse.infomail.security.config.SecurityConstants.*;

@Component
public class JwtUtil {

	public String createAccessToken(HttpServletRequest request, Authentication authentication) {
		AppUser user = (AppUser) authentication.getPrincipal();
		Algorithm algorithm = Algorithm.HMAC256(SECRET.getBytes());
		return JWT.create()
				.withSubject(user.getUsername()) // actually email!
				.withIssuedAt(new Date(System.currentTimeMillis()))
				.withExpiresAt(new Date(System.currentTimeMillis() + TOKEN_LIFETIME))
				.withIssuer(request.getServletPath())
				.withClaim(
						ROLES_CLAIM, user.getAuthorities().stream()
									.map(GrantedAuthority::getAuthority)
									.collect(Collectors.toList())
				).withClaim(
						USER_ID_CLAIM, user.getUserId()
				)
				.sign(algorithm);
	}

	public DecodedJWT verifyAccessToken(String authorizationHeader) throws JWTVerificationException {
		String token = authorizationHeader.substring(TOKEN_PREFIX.length());
		Algorithm algorithm = Algorithm.HMAC256(SECRET.getBytes());
		JWTVerifier verifier = JWT.require(algorithm).build();
		return verifier.verify(token);
	}

}
