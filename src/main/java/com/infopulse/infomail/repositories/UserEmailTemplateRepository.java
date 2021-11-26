package com.infopulse.infomail.repositories;


import com.infopulse.infomail.models.mail.UserEmailTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserEmailTemplateRepository extends JpaRepository<UserEmailTemplate, Long> {

	Optional<UserEmailTemplate> findByIdAndAppUser_Email(Long id, String userEmail);

	Optional<UserEmailTemplate> findBySharingLink(String sharingLink);

	List<UserEmailTemplate> findAllByAppUser_Email(String userEmail);

	void deleteByIdAndAppUser_Email(Long id, String userEmail);

	void deleteAllByAppUser_EmailAndIdIn(String userEmail, List<Long> ids);
}
