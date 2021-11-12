package com.infopulse.infomail.controllers.rest;

import com.infopulse.infomail.dto.exeptions.MessageDTO;
import com.infopulse.infomail.dto.securityRequests.RegistrationRequest;
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
	public MessageDTO register(@Valid @RequestBody RegistrationRequest request) throws IOException {
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
	public MessageDTO confirm(@RequestParam("token") String token) {
		return registrationService.confirmToken(token);
	}

	@GetMapping(path = "reject")
	public MessageDTO reject(@RequestParam("token") String token) {
		return registrationService.rejectToken(token);
	}

}
