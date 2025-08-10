package com.salesmanager.core.business.services.system;

import java.util.ArrayList;
import java.util.Optional;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import java.util.List;
import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.business.repositories.system.SystemConfigurationRepository;
import com.salesmanager.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.salesmanager.core.model.system.SystemConfiguration;



@Service("systemConfigurationService")
public class SystemConfigurationServiceImpl extends
		SalesManagerEntityServiceImpl<Long, SystemConfiguration> implements
		SystemConfigurationService {

	
	private SystemConfigurationRepository systemConfigurationReposotory;
	
	@Inject
	public SystemConfigurationServiceImpl(
			SystemConfigurationRepository systemConfigurationReposotory) {
			super(systemConfigurationReposotory);
			this.systemConfigurationReposotory = systemConfigurationReposotory;
	}
	
	public SystemConfiguration getByKey(String key) throws ServiceException {
		return systemConfigurationReposotory.findByKey(key);
	}
	

	public SystemConfiguration updateByKey(Long id, String value) throws ServiceException {
		Optional<SystemConfiguration> optionalRecord = systemConfigurationReposotory.findById(id);

		if (!optionalRecord.isPresent()) {
			throw new ServiceException("SystemConfiguration with ID " + id + " not found.");
		}

		SystemConfiguration record = optionalRecord.get();
		record.setValue(value);
		return systemConfigurationReposotory.save(record);
	}


	@Override
	public List<SystemConfiguration> getAllConfigurations() throws ServiceException {
		return systemConfigurationReposotory.findAll();
	}



}
