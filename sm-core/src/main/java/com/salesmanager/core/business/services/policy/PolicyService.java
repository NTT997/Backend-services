package com.salesmanager.core.business.services.policy;

import java.util.List;

import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.business.services.common.generic.SalesManagerEntityService;
import com.salesmanager.core.model.policy.Policy;

public interface PolicyService extends SalesManagerEntityService<Long, Policy> {
	List<Policy> getAll() throws ServiceException;

	Policy getById(long Id);

	Policy getByCreatedBy();
}
