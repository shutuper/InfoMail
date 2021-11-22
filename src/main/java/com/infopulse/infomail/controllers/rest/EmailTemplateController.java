package com.infopulse.infomail.controllers.rest;

import com.infopulse.infomail.dto.mail.EmailTemplateDTO;
import com.infopulse.infomail.services.mail.EmailTemplateService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/templates")
@AllArgsConstructor
public class EmailTemplateController {

	private final EmailTemplateService templateService;

	@PostMapping
	public ResponseEntity addTemplate(@Valid @RequestBody EmailTemplateDTO templateDTO, Authentication authentication) {
		try {
			Long userId = (Long) authentication.getCredentials();
			templateService.saveEmailTemplate(templateDTO, userId);
			return ResponseEntity.ok().build();
		} catch (Exception ex) {
			return ResponseEntity.badRequest().build();
		}
	}

	@GetMapping
	public ResponseEntity<List<EmailTemplateDTO>> getTemplates(Authentication authentication) {
		try {
			String userEmail = authentication.getName();
			return ResponseEntity.ok(templateService.getEmailTemplates(userEmail));
		} catch (Exception ex) {
			return ResponseEntity.badRequest().build();
		}
	}
}
