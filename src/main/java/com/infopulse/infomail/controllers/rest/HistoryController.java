package com.infopulse.infomail.controllers.rest;

import com.infopulse.infomail.dto.api.EmailsIdsDTO;
import com.infopulse.infomail.dto.api.HistoryDTO;
import com.infopulse.infomail.services.mail.EmailLogService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/history")
@AllArgsConstructor
public class HistoryController {

	private final EmailLogService emailLogService;

	@GetMapping
	public ResponseEntity<List<HistoryDTO>> getPaginatedEmailsHistory(@RequestParam("page") Integer page,
	                                                                  @RequestParam("rows") Integer rows,
	                                                                  @RequestParam("sortOrder") Integer sortOrder,
	                                                                  @RequestParam("sortField") String sortField,
	                                                                  Authentication authentication) {
		try {
			String senderEmail = (String) authentication.getPrincipal();
			return ResponseEntity.ok(emailLogService
					.getPaginatedEmailsHistory(page, rows, sortOrder, sortField, senderEmail));

		} catch (Exception ex) {
			return ResponseEntity.badRequest().build();
		}
	}

	@GetMapping("total")
	public ResponseEntity<Integer> getTotalNumberOfUserEmails(Authentication authentication) {

		try {
			String senderEmail = (String) authentication.getPrincipal();
			return ResponseEntity.ok(emailLogService.getTotalNumberOfUserEmails(senderEmail));
		} catch (Exception ex) {
			return ResponseEntity.badRequest().build();
		}

	}

	@PutMapping("{id}")
	public ResponseEntity<HistoryDTO> retryFailedEmail(@PathVariable("id") Long id,
	                                                   Authentication authentication) {
		try {
			String userEmail = (String) authentication.getPrincipal();
			HistoryDTO historyDTO = emailLogService.retryFailedEmail(id, userEmail);
			return ResponseEntity.ok(historyDTO);
		} catch (Exception ex) {
			return ResponseEntity.badRequest().build();
		}
	}

	@DeleteMapping("{id}")
	public ResponseEntity<?> deleteEmailById(@PathVariable("id") Long id, Authentication authentication) {
		try {
			String userEmail = (String) authentication.getPrincipal();
			emailLogService.deleteByIdAndUserEmail(id, userEmail);
			return ResponseEntity.ok().build();
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			return ResponseEntity.badRequest().build();
		}
	}

	@DeleteMapping
	public ResponseEntity<?> deleteAllEmailsByIds(@RequestBody EmailsIdsDTO ids, Authentication authentication) {
		try {
			String userEmail = (String) authentication.getPrincipal();
			emailLogService.deleteAllByIdsAndUserEmail(ids, userEmail);
			return ResponseEntity.ok().build();
		} catch (Exception ex) {
			return ResponseEntity.badRequest().build();
		}
	}
}
