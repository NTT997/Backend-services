package com.salesmanager.core.business.services.system;

import static org.mockito.ArgumentMatchers.contains;

import java.util.List;
import javax.inject.Inject;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.business.repositories.system.MerchantConfigurationRepository;
import com.salesmanager.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.reference.language.Language;
import com.salesmanager.core.model.system.IntegrationConfiguration;
import com.salesmanager.core.model.system.MerchantConfig;
import com.salesmanager.core.model.system.MerchantConfiguration;
import com.salesmanager.core.model.system.MerchantConfigurationType;

@Service("merchantConfigurationService")
public class MerchantConfigurationServiceImpl extends SalesManagerEntityServiceImpl<Long, MerchantConfiguration>
		implements MerchantConfigurationService {

	private MerchantConfigurationRepository merchantConfigurationRepository;

	@Inject
	public MerchantConfigurationServiceImpl(MerchantConfigurationRepository merchantConfigurationRepository) {
		super(merchantConfigurationRepository);
		this.merchantConfigurationRepository = merchantConfigurationRepository;
	}

	@Override
	public MerchantConfiguration getMerchantConfiguration(String key, MerchantStore store) throws ServiceException {
		return merchantConfigurationRepository.findByMerchantStoreAndKey(store.getId(), key);
	}

	@Override
	public List<MerchantConfiguration> listByStore(MerchantStore store) throws ServiceException {
		return merchantConfigurationRepository.findByMerchantStore(store.getId());
	}

	@Override
	public List<MerchantConfiguration> listByType(MerchantConfigurationType type, MerchantStore store)
			throws ServiceException {
		return merchantConfigurationRepository.findByMerchantStoreAndType(store.getId(), type);
	}

	@Override
	public void saveOrUpdate(MerchantConfiguration entity) throws ServiceException {

		if (entity.getId() != null && entity.getId() > 0) {
			super.update(entity);
		} else {
			super.create(entity);

		}
	}

	@Override
	public void delete(MerchantConfiguration merchantConfiguration) throws ServiceException {
		MerchantConfiguration config = merchantConfigurationRepository.getOne(merchantConfiguration.getId());
		if (config != null) {
			super.delete(config);
		}
	}

	@Override
	public MerchantConfig getMerchantConfig(MerchantStore store) throws ServiceException {

		MerchantConfiguration configuration = merchantConfigurationRepository.findByMerchantStoreAndKey(store.getId(),
				MerchantConfigurationType.CONFIG.name());

		MerchantConfig config = null;
		if (configuration != null) {
			String value = configuration.getValue();

			ObjectMapper mapper = new ObjectMapper();
			try {
				config = mapper.readValue(value, MerchantConfig.class);
			} catch (Exception e) {
				throw new ServiceException("Cannot parse json string " + value);
			}
		}
		return config;

	}

	@Override
	public void saveMerchantConfig(MerchantConfig config, MerchantStore store) throws ServiceException {

		MerchantConfiguration configuration = merchantConfigurationRepository.findByMerchantStoreAndKey(store.getId(),
				MerchantConfigurationType.CONFIG.name());

		if (configuration == null) {
			configuration = new MerchantConfiguration();
			configuration.setMerchantStore(store);
			configuration.setKey(MerchantConfigurationType.CONFIG.name());
			configuration.setMerchantConfigurationType(MerchantConfigurationType.CONFIG);
		}

		String value = config.toJSONString();
		configuration.setValue(value);
		if (configuration.getId() != null && configuration.getId() > 0) {
			super.update(configuration);
		} else {
			super.create(configuration);

		}

	}

	// huy
	@Override
	public void saveMerchantConfig(List<IntegrationConfiguration> configs, MerchantStore store, Language language)
			throws ServiceException {
		MerchantConfiguration merchantConfiguration = merchantConfigurationRepository.findByMerchantStoreAndKey(store.getId(), MerchantConfigurationType.INTEGRATION.name());
		
		if(merchantConfiguration == null ) {
			merchantConfiguration = new MerchantConfiguration();
			merchantConfiguration.setMerchantStore(store);
			merchantConfiguration.setKey(MerchantConfigurationType.INTEGRATION.name());
			merchantConfiguration.setKey(MerchantConfigurationType.INTEGRATION.name());
		}
		
		try {
	        ObjectMapper mapper = new ObjectMapper();
	        String value = mapper.writeValueAsString(configs);
	        merchantConfiguration.setValue(value);

	        if (merchantConfiguration.getId() != null && merchantConfiguration.getId() > 0) {
	            super.update(merchantConfiguration);
	        } else {
	            super.create(merchantConfiguration);
	        }

	    } catch (JsonProcessingException e) {
	        throw new ServiceException("Error", e);
	    }
	}

}
