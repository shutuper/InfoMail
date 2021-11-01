package com.infopulse.infomail.repositories;

import com.infopulse.infomail.models.tokens.ConfirmationToken;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ConfirmationTokenRepository
		extends JpaRepository<ConfirmationToken, Long> {
	Optional<ConfirmationToken> findByToken(String token);

	@Transactional
	@Modifying
	@Query("UPDATE ConfirmationToken c " +
			"SET c.confirmedAt = ?2 " +
			"WHERE c.token = ?1")
	void updateConfirmedAt(String token,
	                       LocalDateTime confirmedAt);

	@Transactional
	void deleteConfirmationTokenByAppUserUserId(Long appUserId);

	@Transactional
	void deleteConfirmationTokenByTokenId(Long tokenId);

	@Transactional
	Optional<ConfirmationToken> findByAppUserUserId(Long appUserId);

}
