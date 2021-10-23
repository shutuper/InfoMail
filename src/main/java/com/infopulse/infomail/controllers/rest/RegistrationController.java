package com.infopulse.infomail.controllers.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infopulse.infomail.models.dto.SimpleMessageDto;
import com.infopulse.infomail.models.requestBodies.RegistrationRequest;
import com.infopulse.infomail.services.registration.RegistrationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(path = "api/v1/registration")
public class RegistrationController {

	private final RegistrationService registrationService;

	@PostMapping
	public SimpleMessageDto register(@Valid @RequestBody RegistrationRequest request) throws IOException {
			return registrationService.register(request);

	}
	// security test controller
	@GetMapping(path = "sayHi")
	public String sayHi(Authentication authentication) {
		if (authentication!=null)
		return "hi " + authentication.getPrincipal();
		else return "hi anonymous";
	}

	@GetMapping(path = "confirm")
	public SimpleMessageDto confirm(@RequestParam("token") String token) {
		return registrationService.confirmToken(token);
	}

	@GetMapping(path = "reject")
	public SimpleMessageDto reject(@RequestParam("token") String token) {
		return registrationService.rejectToken(token);
	}

}
