package com.infopulse.infomail.repositories;

import com.infopulse.infomail.models.mail.AppUserEmailsInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserEmailsInfoRepository extends JpaRepository<AppUserEmailsInfo, Long> {

	Optional<AppUserEmailsInfo> findByJobName(String jobName);

}
