package com.salesmanager.core.business.services.system;

import java.util.List;

import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.business.services.common.generic.SalesManagerEntityService;
import com.salesmanager.core.model.system.SystemConfiguration;

public interface SystemConfigurationService extends
		SalesManagerEntityService<Long, SystemConfiguration> {
	
	SystemConfiguration getByKey(String key) throws ServiceException;
	List<SystemConfiguration> getAllConfigurations() throws ServiceException;
	SystemConfiguration updateByKey(Long id, String value)throws ServiceException;		
}
