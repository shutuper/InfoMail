package com.infopulse.infomail.repositories;

import com.infopulse.infomail.models.mail.EmailLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmailLogRepository extends JpaRepository<EmailLog, Long> {

	Integer countBySenderEmail(String senderEmail);

	Page<EmailLog> findAllBySenderEmail(String senderEmail, Pageable pageable);

	void deleteByIdAndSenderEmail(Long id, String userEmail);

	Optional<EmailLog> findByIdAndSenderEmail(Long id, String userEmail);

	void deleteAllBySenderEmailAndIdIn(String senderEmail, List<Long> ids);

}
