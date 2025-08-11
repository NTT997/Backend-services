package com.salesmanager.core.business.services.order.orderrequest;

import java.util.List;

import com.salesmanager.core.model.order.orderrequest.OrderRequest;

public interface OrderRequestService{
	
	void createOrderRequest(OrderRequest o);
	
	List<OrderRequest> listByEmail(String email);
	
	void save(OrderRequest o);
	
	OrderRequest getById(Long id);

}
