package com.salesmanager.core.business.repositories.log;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.salesmanager.core.model.log.AppLog;

@Repository
public interface AppLogRepositry extends JpaRepository<AppLog,Long>{

}
