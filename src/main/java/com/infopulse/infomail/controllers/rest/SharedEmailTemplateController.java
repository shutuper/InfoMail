package com.infopulse.infomail.controllers.rest;

import com.infopulse.infomail.dto.api.templates.UserEmailTemplateDTO;
import com.infopulse.infomail.services.mail.UserEmailTemplateService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/sharedtemplates")
@AllArgsConstructor
public class SharedEmailTemplateController {

	private final UserEmailTemplateService templateService;

	@GetMapping("/{sharingId}")
	public ResponseEntity<UserEmailTemplateDTO> getTemplateBySharingId(@PathVariable("sharingId") String sharingId,
	                                                                   Authentication authentication) {
		String userEmail = authentication.getName();

		return ResponseEntity.ok(templateService
				.getTemplateAsDtoBySharingId(sharingId, userEmail));
	}

	@PostMapping("/{sharingId}")
	public ResponseEntity<?> saveTemplateBySharingId(@PathVariable("sharingId") String sharingId,
												Authentication authentication) {
		templateService.saveTemplateBySharingId(sharingId, authentication);
		return ResponseEntity.ok().build();
	}

}
