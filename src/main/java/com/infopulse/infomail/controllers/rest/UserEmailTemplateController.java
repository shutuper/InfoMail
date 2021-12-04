package com.infopulse.infomail.controllers.rest;

import com.infopulse.infomail.dto.api.templates.EmailTemplateDTO;
import com.infopulse.infomail.dto.api.templates.EmailTemplatesIdsDTO;
import com.infopulse.infomail.dto.api.templates.UserEmailTemplateDTO;
import com.infopulse.infomail.dto.api.templates.UserTemplatesOptionsDTO;
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
@RequestMapping("/api/v1/usertemplates")
@AllArgsConstructor
public class UserEmailTemplateController {

	private final UserEmailTemplateService templateService;

	@PostMapping
	public ResponseEntity<UserEmailTemplateDTO> addTemplate(@Valid @RequestBody UserEmailTemplateDTO templateDTO, Authentication authentication) {
		try {
			UserEmailTemplateDTO dto = templateService.saveEmailTemplate(templateDTO, authentication);
			return ResponseEntity.ok(dto);
		} catch (Exception ex) {
			return ResponseEntity.badRequest().build();
		}
	}

	@GetMapping("options")
	public ResponseEntity<List<UserTemplatesOptionsDTO>> getAllAsOptions(Authentication authentication) {
		try {
			String userEmail = authentication.getName();
			return ResponseEntity.ok(templateService.getAllTemplatesAsOptions(userEmail));
		} catch (Exception ex) {
			return ResponseEntity.badRequest().build();
		}
	}

	@GetMapping("{id}/dto")
	public ResponseEntity<EmailTemplateDTO> getEmailTemplateDTO(@PathVariable("id") Long id, Authentication authentication) {
		try {
			String userEmail = authentication.getName();
			return ResponseEntity.ok(templateService.getEmailTemplateDTO(id, userEmail));
		} catch (Exception ex) {
			return ResponseEntity.badRequest().build();
		}
	}

	@GetMapping
	public ResponseEntity<List<UserEmailTemplateDTO>> getPaginatedTemplates(@RequestParam("page") Integer page,
	                                                                        @RequestParam("rows") Integer rows,
	                                                                        @RequestParam("sortOrder") Integer sortOrder,
	                                                                        @RequestParam("sortField") String sortField,
	                                                                        Authentication authentication) {
		try {
			String userEmail = authentication.getName();
			return ResponseEntity.ok(templateService
					.getPaginatedTemplates(page, rows, sortOrder, sortField, userEmail));

		} catch (Exception ex) {
			return ResponseEntity.badRequest().build();
		}
	}

	@GetMapping("total")
	public ResponseEntity<Integer> getTotalNumberOfTemplates(Authentication authentication) {

		try {
			String userEmail = authentication.getName();
			return ResponseEntity.ok(templateService.getTotalNumberOfTemplates(userEmail));
		} catch (Exception ex) {
			return ResponseEntity.badRequest().build();
		}
	}

	@GetMapping("{id}")
	public ResponseEntity<UserEmailTemplateDTO> getTemplateById(@PathVariable("id") Long id, Authentication authentication) {
		try {
			String userEmail = authentication.getName();

			return ResponseEntity.ok(templateService
					.getEmailTemplateAsDtoById(id, userEmail));
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			return ResponseEntity.badRequest().build();
		}
	}

	@GetMapping("/shared/{sharingId}")
	public ResponseEntity<UserEmailTemplateDTO> getTemplateBySharingId(@PathVariable("sharingId") String sharingId, Authentication authentication) {
		try {
			String userEmail = authentication.getName();

			return ResponseEntity.ok(templateService
					.getTemplateAsDtoBySharingId(sharingId, userEmail));
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			return ResponseEntity.badRequest().build();
		}
	}

	@PostMapping("/shared/")
	public ResponseEntity<?> saveSharedTemplate(@Valid @RequestBody UserEmailTemplateDTO templateDTO, Authentication authentication) {
		try {
			templateService.saveSharedTemplate(templateDTO, authentication);
			return ResponseEntity.ok().build();
		} catch (Exception ex) {
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
