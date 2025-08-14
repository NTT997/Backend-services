package com.salesmanager.core.business.services.order.orderrequest;

import java.util.List;

import com.salesmanager.core.model.order.Order;
import com.salesmanager.core.model.order.orderrequest.OrderRequest;
import com.salesmanager.core.model.order.orderrequest.RequestApprovalStatus;

public interface OrderRequestService{
	
	void createOrderRequest(OrderRequest o);
	
	List<OrderRequest> listByEmailAndOptionalStatus(String email, RequestApprovalStatus status);
	
	void save(OrderRequest o);
	
	OrderRequest getById(Long id);

	OrderRequest getByOrder(Order order);

}
