package com.infopulse.infomail.repositories;

import com.infopulse.infomail.models.quartz.QrtzJobDetail;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface QrtzJobDetailRepository extends JpaRepository<QrtzJobDetail, String> {

	@Transactional
//	@Query(value = "select q from qrtz_job_details q where q.job_name=?1", nativeQuery = true)
	Optional<QrtzJobDetail> findByJobName(String jobName);

}
