package com.infopulse.infomail.controllers.rest;

import com.infopulse.infomail.dto.api.EmailTemplatesIdsDTO;
import com.infopulse.infomail.dto.mail.UserEmailTemplateDTO;
import com.infopulse.infomail.models.mail.UserEmailTemplate;
import com.infopulse.infomail.services.mail.UserEmailTemplateService;
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
public class UserEmailTemplateController {

	private final UserEmailTemplateService templateService;

	@PostMapping
	public ResponseEntity<UserEmailTemplateDTO> addTemplate(@Valid @RequestBody UserEmailTemplateDTO templateDTO, Authentication authentication) {
		try {
			UserEmailTemplate template = templateService.saveEmailTemplate(templateDTO, authentication);
			String userEmail = authentication.getName();
			UserEmailTemplateDTO dto = new UserEmailTemplateDTO(
					template.getId(),
					template.getName(),
					template.getSubject(),
					template.getBody(),
					userEmail,
					template.getSharingLink()
			);
			return ResponseEntity.ok(dto);
		} catch (Exception ex) {
			return ResponseEntity.badRequest().build();
		}
	}

	@GetMapping
	public ResponseEntity<List<UserEmailTemplateDTO>> getTemplates(Authentication authentication) {
		try {
			String userEmail = authentication.getName();
			return ResponseEntity.ok(templateService.getEmailTemplates(userEmail));
		} catch (Exception ex) {
			return ResponseEntity.badRequest().build();
		}
	}

	@GetMapping("{id}")
	public ResponseEntity<UserEmailTemplateDTO> getTemplateById(@PathVariable("id") Long id, Authentication authentication) {
		try {
			String userEmail = (String) authentication.getPrincipal();
			UserEmailTemplate template = templateService.getEmailTemplateById(id, userEmail);
			UserEmailTemplateDTO dto = new UserEmailTemplateDTO(
					template.getId(),
					template.getName(),
					template.getSubject(),
					template.getBody(),
					userEmail,
					template.getSharingLink()
			);
			return ResponseEntity.ok(dto);
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			return ResponseEntity.badRequest().build();
		}
	}

	@DeleteMapping("{id}")
	public ResponseEntity<?> deleteById(@PathVariable("id") Long id, Authentication authentication) {
		try {
			String userEmail = (String) authentication.getPrincipal();
			templateService.deleteByIdAndUserEmail(id, userEmail);
			return ResponseEntity.ok().build();
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			return ResponseEntity.badRequest().build();
		}
	}

	@DeleteMapping
	public ResponseEntity<?> deleteAllByIds(@RequestBody EmailTemplatesIdsDTO ids, Authentication authentication) {
		try {
			String userEmail = (String) authentication.getPrincipal();
			templateService.deleteAllByIdsAndUserEmail(ids, userEmail);
			return ResponseEntity.ok().build();
		} catch (Exception ex) {
			return ResponseEntity.badRequest().build();
		}
	}
}
