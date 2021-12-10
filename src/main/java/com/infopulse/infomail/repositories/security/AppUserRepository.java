package com.infopulse.infomail.repositories.security;

import com.infopulse.infomail.models.users.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

	@Transactional
	@Modifying
	@Query("UPDATE AppUser u SET u.isEnabled=true WHERE u.email=?1")
	void enableAppUserByEmail(String email);

	@Query(value = "SELECT email from app_user a where a.is_enabled=true", nativeQuery = true)
	List<String> findAllEmails();

	@Transactional
	Optional<AppUser> findAppUserByEmail(String email);

	void deleteAppUserByUserId(Long userId);

}
