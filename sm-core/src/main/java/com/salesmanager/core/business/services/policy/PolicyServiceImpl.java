package com.salesmanager.core.business.services.policy;

import java.util.List;

import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.business.repositories.policy.PolicyRepository;
import com.salesmanager.core.model.policy.Policy;

public class PolicyServiceImpl implements PolicyService{

	private PolicyRepository policyRepository;
	
	@Override
	public List<Policy> getAll() throws ServiceException {
		return  policyRepository.findAll();
	}

	@Override
	public Policy getById(long Id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Policy getByCreatedBy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(Policy entity) throws ServiceException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveAll(Iterable<Policy> entities) throws ServiceException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Policy entity) throws ServiceException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void create(Policy entity) throws ServiceException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Policy entity) throws ServiceException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Policy getById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Policy> list() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long count() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void flush() {
		// TODO Auto-generated method stub
		
	}

}
