package com.salesmanager.core.mongo_model.common.audit;

public interface Auditable {
	
	AuditSection getAuditSection();
	
	void setAuditSection(AuditSection audit);
}
