package com.salesmanager.core.business.repositories.log;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.salesmanager.core.model.log.AppLog;
import com.salesmanager.core.model.logging.DbLog;

@Repository
public interface DbLogRepository extends JpaRepository<DbLog, Long> {
}

