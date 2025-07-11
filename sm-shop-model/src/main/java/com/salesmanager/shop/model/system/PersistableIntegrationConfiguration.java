package com.salesmanager.shop.model.system;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotBlank;

public class PersistableIntegrationConfiguration {
	@NotBlank
	private String moduleCode;
	
	private boolean active;
	private boolean defaultSelected;
	
	private String environment;
	
	private Map<String, String> integrationKeys;
	
	private Map<String, List<String>> integrationOptions;

	public String getModuleCode() {
		return moduleCode;
	}

	public void setModuleCode(String moduleCode) {
		this.moduleCode = moduleCode;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isDefaultSelected() {
		return defaultSelected;
	}

	public void setDefaultSelected(boolean defaultSelected) {
		this.defaultSelected = defaultSelected;
	}

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public Map<String, String> getIntegrationKeys() {
		return integrationKeys;
	}

	public void setIntegrationKeys(Map<String, String> integrationKeys) {
		this.integrationKeys = integrationKeys;
	}

	public Map<String, List<String>> getIntegrationOptions() {
		return integrationOptions;
	}

	public void setIntegrationOptions(Map<String, List<String>> integrationOptions) {
		this.integrationOptions = integrationOptions;
	}
	
	
}
