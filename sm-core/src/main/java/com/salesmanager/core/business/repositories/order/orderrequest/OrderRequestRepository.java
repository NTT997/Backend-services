package com.salesmanager.core.business.repositories.order.orderrequest;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.salesmanager.core.model.order.orderrequest.OrderRequest;


public interface OrderRequestRepository extends JpaRepository<OrderRequest, Long> {

	@Query("SELECT DISTINCT o FROM OrderRequest o " +
	           "JOIN o.listOrderRequestApproval a " +
	           "WHERE a.approvedBy = :email")
	List<OrderRequest> findByEmail(@Param("email") String email);
}
