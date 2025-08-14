package com.salesmanager.core.business.repositories.order;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.salesmanager.core.model.order.Order;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom {

    @Query("select o from Order o join fetch o.merchant om "
    		+ "join fetch o.orderProducts op "
    		+ "left join fetch o.delivery od left join fetch od.country left join fetch od.zone "
    		+ "left join fetch o.billing ob left join fetch ob.country left join fetch ob.zone "
    		+ "left join fetch o.orderAttributes oa "
    		+ "join fetch o.orderTotal ot left "
    		+ "join fetch o.orderHistory oh left "
    		+ "join fetch op.downloads opd left "
    		+ "join fetch op.orderAttributes opa "
    		+ "left join fetch op.prices opp where o.id = ?1 and om.id = ?2")
	Order findOne(Long id, Integer merchantId);

//    @Query("SELECT o FROM Order o "
//    		+ "JOIN OrderRequest req ON req.order.id = o.id "
//    		+ "WHERE req.status = com.salesmanager.core.model.order.orderrequest.OrderRequestStatus.REJECTED "
//    		+ "AND o.audit.modifiedBy = :email"
//    )
//	List<Order> findAllOrderRejectedByEmail(@Param("email")String email);
    
    @Query("SELECT o FROM OrderRequest req " +
    	       "JOIN req.order o " +
    	       "WHERE req.status = com.salesmanager.core.model.order.orderrequest.OrderRequestStatus.REJECTED " +
    	       "AND o.audit.modifiedBy = :email")
    List<Order> findAllOrderRejectedByEmail(@Param("email") String email);
    
}
