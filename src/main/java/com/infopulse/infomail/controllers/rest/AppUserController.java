package com.infopulse.infomail.controllers.rest;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RestController
@SecurityRequirement(name = "Authorization")
@RequestMapping("api/v1/users")
@AllArgsConstructor
public class AppUserController {

	@GetMapping(path = "email")
	public Map<String, String> getUserEmail(Authentication authentication) {
		String email = (String) authentication.getPrincipal();
		return new HashMap<>() {{
			put("email", email);
		}};
	}

	@GetMapping// if user is not authed => authentication = null
	public Boolean isAuthed(Authentication authentication) {
		log.info("User {} is checking for authentication", authentication);
		return Objects.nonNull(authentication);
	}

}
