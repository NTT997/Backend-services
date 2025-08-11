package com.salesmanager.core.business.repositories.system;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.salesmanager.core.model.system.SystemConfiguration;

public interface SystemConfigurationRepository extends JpaRepository<SystemConfiguration, Long> {


	SystemConfiguration findByKey(String key);

	@Query("""
		SELECT sc
		FROM SystemConfiguration sc
		WHERE sc.key = 'ORDER'
		 AND sc.min <= :orderTotal
		 AND (sc.max IS NULL OR sc.max > :orderTotal)
	""")
	SystemConfiguration findConfigByTotal(@Param("orderTotal") long orderTotal);

}
