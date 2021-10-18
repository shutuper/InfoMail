package com.infopulse.infomail.controllers.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infopulse.infomail.models.requestBodies.RegistrationRequest;
import com.infopulse.infomail.services.RegistrationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(path = "api/v1/registration")
public class RegistrationController {

	private final RegistrationService registrationService;

	@PostMapping
	public String register(@Valid @RequestBody RegistrationRequest request, HttpServletResponse response) throws IOException {
		try {
			return registrationService.register(request);
		} catch (IllegalStateException e) {
			log.error(e.getMessage(), e);
			response.setHeader("registration_error", e.getMessage());
			response.setStatus(HttpStatus.BAD_REQUEST.value());
			Map<String, String> error = new HashMap<>();
			error.put("error_message", e.getMessage());
			response.setContentType(APPLICATION_JSON_VALUE);
			new ObjectMapper().writeValue(response.getOutputStream(), error);
		}
		return null;
	}

	@GetMapping(path = "sayHi")
	public String sayHi(HttpServletRequest request) {
		return "hi " + request.getUserPrincipal();
	}

	@GetMapping(path = "confirm")
	public String confirm(@RequestParam("token") String token) {
		return registrationService.confirmToken(token);
	}

	@GetMapping(path = "reject")
	public String reject(@RequestParam("token") String token) {
		return registrationService.rejectToken(token);
	}

}
