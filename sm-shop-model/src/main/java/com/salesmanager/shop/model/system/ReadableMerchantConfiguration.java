package com.salesmanager.shop.model.system;

import java.util.List;

import com.salesmanager.shop.model.entity.Entity;

public class ReadableMerchantConfiguration extends Entity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean active;
	private List<Object> values;
	private String key;
	private String merchantStore;
	
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public List<Object> getValues() {
		return values;
	}
	public void setValues(List<Object> values) {
		this.values = values;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getMerchantStore() {
		return merchantStore;
	}
	public void setMerchantStore(String merchantStore) {
		this.merchantStore = merchantStore;
	}
	
	
}
