package com.salesmanager.core.business.services.order.orderrequest;


import java.util.List;

import org.springframework.stereotype.Service;

import com.salesmanager.core.business.repositories.order.orderrequest.OrderRequestRepository;
import com.salesmanager.core.model.order.Order;
import com.salesmanager.core.model.order.orderrequest.OrderRequest;
import com.salesmanager.core.model.order.orderrequest.RequestApprovalStatus;


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
	public List<OrderRequest> listByEmailAndOptionalStatus(String email, RequestApprovalStatus status) {
		return orderRequestRepository.findByApproverAndOptionalStatus(email, status);
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

	@Override
	public OrderRequest getByOrder(Order order) {
		return orderRequestRepository.findByOrder(order);
	}

}
