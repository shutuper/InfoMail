package com.infopulse.infomail.repositories;

import com.infopulse.infomail.models.quartz.QrtzJobDetail;
import com.infopulse.infomail.models.quartz.QrtzJobDetailId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface QrtzJobDetailRepository extends JpaRepository<QrtzJobDetail, QrtzJobDetailId> {

	@Transactional
	@Query(value = "select q from qrtz_job_details q where q.job_name=?1", nativeQuery = true)
	Optional<QrtzJobDetail> findById_JobName(String jobName);

}
