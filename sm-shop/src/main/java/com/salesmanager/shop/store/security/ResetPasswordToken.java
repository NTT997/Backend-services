package com.salesmanager.shop.store.security;

import java.io.Serializable;

public class ResetPasswordToken implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String token;
	private String store;

	public ResetPasswordToken() {
        super();
    }

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getStore() {
		return store;
	}

	public void setStore(String store) {
		this.store = store;
	}
}
