package com.salesmanager.shop.mapper.configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.json.JSONArray;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.reference.language.Language;import com.salesmanager.core.model.system.IntegrationConfiguration;
import com.salesmanager.core.model.system.MerchantConfiguration;
import com.salesmanager.shop.mapper.Mapper;
import com.salesmanager.shop.model.system.ReadableIntegrationConfiguration;
import com.salesmanager.shop.model.system.ReadableMerchantConfiguration;

//huy

@Component
public class ReadableMerchantConfigurationMapper implements Mapper<MerchantConfiguration, ReadableMerchantConfiguration>{

	@Override
	public ReadableMerchantConfiguration convert(MerchantConfiguration source, MerchantStore store, Language language) {
		
		ReadableMerchantConfiguration readbleMerchantConfig = new ReadableMerchantConfiguration();
		
		
		return this.merge(source, readbleMerchantConfig, store, language);
	}

	@Override
	public ReadableMerchantConfiguration merge(MerchantConfiguration source, ReadableMerchantConfiguration destination,
			MerchantStore store, Language language) {
		
		if(destination == null) {
			destination = new ReadableMerchantConfiguration();
		}
		
		destination.setId(source.getId());
		destination.setActive(source.getActive());
		destination.setKey(source.getKey());
		destination.setMerchantStore(source.getMerchantStore().getCode());

		JSONArray jsonArr = new JSONArray(source.getValue());
		ObjectMapper mapper = new ObjectMapper();
		
		List<ReadableIntegrationConfiguration> listIntegrationConfig = null;
		try {
			listIntegrationConfig = mapper.readValue(jsonArr.toString(), new TypeReference<List<ReadableIntegrationConfiguration>>() {});
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} 
		destination.setValues(new ArrayList<>(listIntegrationConfig));
		
		return destination;
	}
	

}
