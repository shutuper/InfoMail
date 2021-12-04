package com.infopulse.infomail.repositories.templates;


import com.infopulse.infomail.models.templates.EmailTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailTemplateRepository extends JpaRepository<EmailTemplate, Long> {

    Optional<EmailTemplate> findByIdAndAppUser_Email(Long id, String userEmail);

}
