package com.infopulse.infomail.repositories;

import com.infopulse.infomail.models.mail.AppRecipient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AppRecipientRepository extends JpaRepository<AppRecipient, Long> {


	List<AppRecipient> findAllByUserInfo_Id(Long userInfoId);

	List<AppRecipient> findAllByUserInfo_QrtzJobDetail_JobName(String jobName);
}
