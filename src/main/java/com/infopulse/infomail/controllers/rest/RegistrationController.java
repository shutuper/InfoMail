package com.infopulse.infomail.controllers.rest;

import com.infopulse.infomail.dto.SimpleMessageDto;
import com.infopulse.infomail.dto.RegistrationRequest;
import com.infopulse.infomail.services.registration.RegistrationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

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
		if (authentication != null)
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
