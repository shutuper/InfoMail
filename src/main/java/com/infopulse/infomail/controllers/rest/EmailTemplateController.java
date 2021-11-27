package com.infopulse.infomail.controllers.rest;

import com.infopulse.infomail.dto.mail.EmailTemplateDTO;
import com.infopulse.infomail.models.mail.EmailTemplate;
import com.infopulse.infomail.services.mail.EmailTemplateService;
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
@RequestMapping("/api/v1/templates")
@AllArgsConstructor
public class EmailTemplateController {

	private final EmailTemplateService templateService;

	@GetMapping("{id}")
	public ResponseEntity<EmailTemplateDTO> getTemplateById(@PathVariable("id") Long id,
	                                                        Authentication authentication) {
		try {
			String userEmail = (String) authentication.getPrincipal();
			EmailTemplate template = templateService.getEmailTemplateById(id, userEmail);
			EmailTemplateDTO dto = new EmailTemplateDTO(
					template.getId(),
					template.getSubject(),
					template.getBody()
			);
			return ResponseEntity.ok(dto);
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			return ResponseEntity.badRequest().build();
		}
	}

}
