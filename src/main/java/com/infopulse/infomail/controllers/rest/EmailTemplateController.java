package com.infopulse.infomail.controllers.rest;

import com.infopulse.infomail.dto.api.templates.EmailTemplateDTO;
import com.infopulse.infomail.services.mail.EmailTemplateService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@SecurityRequirement(name = "Authorization")
@RequestMapping("/api/v1/templates")
@AllArgsConstructor
public class EmailTemplateController {

	private final EmailTemplateService templateService;

	@GetMapping("{id}")
	public ResponseEntity<EmailTemplateDTO> getTemplateById(@PathVariable("id") Long id,
	                                                        Authentication authentication) {
		String userEmail = (String) authentication.getPrincipal();

		return ResponseEntity.ok(templateService
				.getEmailTemplateAsDtoById(id, userEmail));
	}

}
