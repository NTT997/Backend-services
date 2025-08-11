package com.salesmanager.core.model.order.orderrequest;

public enum ConfigType {
	
	ORDERTYPE1(500_000L),
	ORDERTYPE2(1_000_000L),
	ORDERTYPE3(5_000_000L);

	private final Long value;

	ConfigType(Long value) {
		this.value = value;
	}

	public Long getValue() {
		return value;
	}

}
