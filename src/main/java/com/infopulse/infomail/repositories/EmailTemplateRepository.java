package com.infopulse.infomail.repositories;


import com.infopulse.infomail.models.mail.EmailTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmailTemplateRepository extends JpaRepository<EmailTemplate, Long> {

	Optional<EmailTemplate> findById(Long id);
	List<EmailTemplate> findAllByAppUser_Email(String email);
}
