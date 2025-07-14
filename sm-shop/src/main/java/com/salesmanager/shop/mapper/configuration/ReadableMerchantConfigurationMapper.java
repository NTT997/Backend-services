package com.salesmanager.shop.mapper.configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.reference.language.Language;
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
			if(source.getKey().equalsIgnoreCase("PAYMENT")) {
				
				listIntegrationConfig = mapper.readValue(jsonArr.toString(), new TypeReference<List<ReadableIntegrationConfiguration>>() {});
				destination.setValues(new ArrayList<>(listIntegrationConfig));
			}
			
			else if (source.getKey().equalsIgnoreCase("SHIPPING")) {
			    JSONArray jsonArray = new JSONArray(source.getValue());
			    List<Object> list = new ArrayList<>();

			    for (int i = 0; i < jsonArray.length(); i++) {
			        JSONObject jsonObj = jsonArray.getJSONObject(i);
			        Map<String, Object> map = new HashMap<>();

			        for (String key : jsonObj.keySet()) {
			            Object value = jsonObj.get(key);

			            if (value instanceof JSONArray arr) {
			                List<Object> subList = new ArrayList<>();
			                for (int j = 0; j < arr.length(); j++) {
			                    Object subItem = arr.get(j);
			                    if (subItem instanceof JSONObject subObj) {
			                        Map<String, Object> subMap = new HashMap<>();
			                        for (String subKey : subObj.keySet()) {
			                            subMap.put(subKey, subObj.get(subKey));
			                        }
			                        subList.add(subMap); // add subObjecy
			                    } else { 
			                        subList.add(subItem); //add primitive binh thuong 
			                    }
			                }
			                map.put(key, subList);

			            } else {
			                map.put(key, jsonObj.get(key));
			            }
			        }

			        list.add(map);
			    }

			    destination.setValues(list);
			}

		
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} 
		
		return destination;
	}
	 

}
