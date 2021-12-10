package com.infopulse.infomail.controllers.rest;

import com.infopulse.infomail.dto.api.emails.ExecutedEmailDTO;
import com.infopulse.infomail.dto.api.templates.EmailWithTemplateDTO;
import com.infopulse.infomail.dto.app.IdsDTO;
import com.infopulse.infomail.services.mail.EmailLogService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/history")
@AllArgsConstructor
public class HistoryController {

	private final EmailLogService emailLogService;

	@GetMapping
	public ResponseEntity<List<ExecutedEmailDTO>> getPaginatedEmailsHistory(@RequestParam("page") Integer page,
	                                                                        @RequestParam("rows") Integer rows,
	                                                                        @RequestParam("sortOrder") Integer sortOrder,
	                                                                        @RequestParam("sortField") String sortField,
	                                                                        Authentication authentication) {
		String senderEmail = (String) authentication.getPrincipal();

		return ResponseEntity.ok(emailLogService
				.getPaginatedEmailsHistory(page, rows, sortOrder, sortField, senderEmail));
	}

	@GetMapping("{id}")
	public ResponseEntity<EmailWithTemplateDTO> getEmailWithTemplateById(@PathVariable("id") Long id,
	                                                                     Authentication authentication) {
		String senderEmail = (String) authentication.getPrincipal();

		return ResponseEntity.ok(emailLogService
				.getEmailWithTemplateDTO(id, senderEmail));
	}

	@GetMapping("total")
	public ResponseEntity<Integer> getTotalNumberOfUserEmails(Authentication authentication) {
		String senderEmail = (String) authentication.getPrincipal();

		return ResponseEntity.ok(emailLogService
				.getTotalNumberOfUserEmails(senderEmail));
	}

	@PutMapping("{id}")
	public ResponseEntity<ExecutedEmailDTO> retryFailedEmail(@PathVariable("id") Long id,
	                                                         Authentication authentication) {
		String senderEmail = (String) authentication.getPrincipal();

		return ResponseEntity.ok(emailLogService
				.retryFailedEmail(id, senderEmail));
	}

	@DeleteMapping("{id}")
	public ResponseEntity<?> deleteEmailById(@PathVariable("id") Long id, Authentication authentication) {
		String senderEmail = (String) authentication.getPrincipal();

		emailLogService.deleteByIdAndUserEmail(id, senderEmail);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping
	public ResponseEntity<?> deleteAllEmailsByIds(@Valid @RequestBody IdsDTO ids, Authentication authentication) {
		String senderEmail = (String) authentication.getPrincipal();

		emailLogService.deleteAllByIdsAndUserEmail(ids, senderEmail);
		return ResponseEntity.ok().build();
	}
}
