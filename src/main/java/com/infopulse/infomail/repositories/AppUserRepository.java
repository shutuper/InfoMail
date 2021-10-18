package com.infopulse.infomail.repositories;

import com.infopulse.infomail.models.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

	@Transactional
	@Modifying
	@Query("UPDATE AppUser u SET u.isEnabled=true WHERE u.email=?1")
	void enableAppUserByEmail(String email);

	@Transactional
	Optional<AppUser> findAppUserByEmail(String email);

	Optional<AppUser> findAppUserByUserId(Long userId);

	void deleteAppUserByUserId(Long userId);

}
