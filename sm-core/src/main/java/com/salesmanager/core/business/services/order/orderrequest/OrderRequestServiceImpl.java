package com.salesmanager.core.business.services.order.orderrequest;


import java.util.List;

import org.springframework.stereotype.Service;

import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.business.repositories.order.orderrequest.OrderRequestRepository;
import com.salesmanager.core.model.order.orderrequest.OrderRequest;


@Service("OrderRequestService")
public class OrderRequestServiceImpl implements OrderRequestService{
	
	
	private final OrderRequestRepository orderRequestRepository;

	public OrderRequestServiceImpl(OrderRequestRepository orderRequestRepository) {
		this.orderRequestRepository = orderRequestRepository;
	}
	
	@Override
	public void createOrderRequest(OrderRequest o) {
		orderRequestRepository.save(o);
	}

	@Override
	public List<OrderRequest> listByEmail(String email) {
		return orderRequestRepository.findByEmail(email);
	}

	@Override
	public OrderRequest getById(Long id) {
		return orderRequestRepository.getById(id);
	}

	@Override
	public void save(OrderRequest o) {
		// TODO Auto-generated method stub
		orderRequestRepository.save(o);
	}
}
