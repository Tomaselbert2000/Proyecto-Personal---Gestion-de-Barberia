package com.repository;

import com.model.ServiceRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRecordRepository extends JpaRepository<ServiceRecord, Long> {
}
