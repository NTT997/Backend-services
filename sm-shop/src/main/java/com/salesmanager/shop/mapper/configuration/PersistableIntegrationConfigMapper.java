package com.salesmanager.shop.mapper.configuration;

import org.springframework.stereotype.Component;

import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.reference.language.Language;
import com.salesmanager.core.model.system.IntegrationConfiguration;
import com.salesmanager.shop.mapper.Mapper;
import com.salesmanager.shop.model.system.PersistableIntegrationConfiguration;

//huy
@Component
public class PersistableIntegrationConfigMapper implements Mapper<PersistableIntegrationConfiguration, IntegrationConfiguration> {

	@Override
	public IntegrationConfiguration convert(PersistableIntegrationConfiguration source, MerchantStore store,
			Language language) {
		IntegrationConfiguration config = new IntegrationConfiguration();
		return this.merge(source, config, store, language);
	}

	@Override
	public IntegrationConfiguration merge(PersistableIntegrationConfiguration source,
			IntegrationConfiguration destination, MerchantStore store, Language language) {
		
		destination.setActive(source.isActive());
		destination.setDefaultSelected(source.isDefaultSelected());
		destination.setEnvironment(source.getEnvironment());
		destination.setIntegrationKeys(source.getIntegrationKeys());
		destination.setIntegrationOptions(source.getIntegrationOptions());
		destination.setModuleCode(source.getModuleCode());
		
		return destination;
	}

}
