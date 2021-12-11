package com.infopulse.infomail.controllers.rest;

import com.infopulse.infomail.dto.api.templates.EmailTemplateDTO;
import com.infopulse.infomail.dto.api.templates.UserEmailTemplateDTO;
import com.infopulse.infomail.dto.api.templates.UserTemplatesOptionsDTO;
import com.infopulse.infomail.dto.app.IdsDTO;
import com.infopulse.infomail.services.mail.UserEmailTemplateService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@SecurityRequirement(name = "Authorization")
@RequestMapping("/api/v1/usertemplates")
@AllArgsConstructor
public class UserEmailTemplateController {

	private final UserEmailTemplateService templateService;

	@PostMapping
	public ResponseEntity<UserEmailTemplateDTO> addTemplate(@Valid @RequestBody UserEmailTemplateDTO templateDTO,
	                                                        Authentication authentication) {
		return ResponseEntity.ok(templateService
				.saveEmailTemplate(templateDTO, authentication));
	}

	@GetMapping("options")
	public ResponseEntity<List<UserTemplatesOptionsDTO>> getAllAsOptions(Authentication authentication) {
		String userEmail = authentication.getName();

		return ResponseEntity.ok(templateService
				.getAllTemplatesAsOptions(userEmail));
	}

	@GetMapping("{id}/dto")
	public ResponseEntity<EmailTemplateDTO> getEmailTemplateDTO(@PathVariable("id") Long id,
	                                                            Authentication authentication) {
		String userEmail = authentication.getName();

		return ResponseEntity.ok(templateService
				.getEmailTemplateDTO(id, userEmail));
	}

	@GetMapping
	public ResponseEntity<List<UserEmailTemplateDTO>> getPaginatedTemplates(@RequestParam("page") Integer page,
	                                                                        @RequestParam("rows") Integer rows,
	                                                                        @RequestParam("sortOrder") Integer sortOrder,
	                                                                        @RequestParam("sortField") String sortField,
	                                                                        Authentication authentication) {
		String userEmail = authentication.getName();

		return ResponseEntity.ok(templateService
				.getPaginatedTemplates(page, rows, sortOrder, sortField, userEmail));
	}

	@GetMapping("total")
	public ResponseEntity<Integer> getTotalNumberOfTemplates(Authentication authentication) {
		String userEmail = authentication.getName();

		return ResponseEntity.ok(templateService
				.getTotalNumberOfTemplates(userEmail));
	}

	@GetMapping("{id}")
	public ResponseEntity<UserEmailTemplateDTO> getTemplateById(@PathVariable("id") Long id,
	                                                            Authentication authentication) {
		String userEmail = authentication.getName();

		return ResponseEntity.ok(templateService
				.getEmailTemplateAsDtoById(id, userEmail));
	}


	@DeleteMapping("{id}")
	public ResponseEntity<?> deleteById(@PathVariable("id") Long id,
	                                    Authentication authentication) {
		String userEmail = (String) authentication.getPrincipal();

		templateService.deleteByIdAndUserEmail(id, userEmail);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping
	public ResponseEntity<?> deleteAllByIds(@Valid @RequestBody IdsDTO ids,
	                                        Authentication authentication) {
		String userEmail = (String) authentication.getPrincipal();

		templateService.deleteAllByIdsAndUserEmail(ids, userEmail);
		return ResponseEntity.ok().build();
	}
}
