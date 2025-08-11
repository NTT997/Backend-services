package com.salesmanager.shop.store.api.v1.order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.business.services.order.orderrequest.OrderRequestService;
import com.salesmanager.core.model.order.orderrequest.OrderRequest;
import com.salesmanager.core.model.order.orderrequest.OrderRequestApproval;
import com.salesmanager.core.model.order.orderrequest.RequestApprovalStatus;
import com.salesmanager.shop.model.order.orderrequest.ReadableOrderRequest;
import com.salesmanager.shop.model.order.orderrequest.ReadableOrderRequestApproval;

import io.swagger.annotations.Api;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;

@RestController
@RequestMapping("/api/v1")
@Api(tags = { "Order Request api (Order Request Flow Api)" })
@SwaggerDefinition(tags = {
		@Tag(name = "Order Request flow resource", description = "Manage orders request (create, list, get)") })
public class OrderRequestApi {

	@Inject
	private OrderRequestService orderRequestService;
	

	/** 
	 * Get List Order Request of User by email
	 * @param email
	 * @return List<OrderReqest>
	 */
	@GetMapping("/private/orders/requests/approver")
	public ResponseEntity<?> getOrderRequestsByApproverEmail(@RequestParam String email) {
		List<OrderRequest> orderRequests = orderRequestService.listByEmail(email);

		// convert to readable order request
		List<ReadableOrderRequest> listReadable = new ArrayList<>();
		for (OrderRequest or : orderRequests) {
			ReadableOrderRequest readableOrderRequest = new ReadableOrderRequest();
			readableOrderRequest.setCode(or.getCode());
			readableOrderRequest.setCreatedAt(or.getCreatedAt());
			readableOrderRequest.setCreatedBy(or.getCreatedBy());
			readableOrderRequest.setId(or.getId());
			readableOrderRequest.setConfigId(or.getSystemConfig().getId());
			
			List<ReadableOrderRequestApproval> readbleApprover = new ArrayList<>();
			for( OrderRequestApproval approvers: or.getListOrderRequestApproval() ) {
				ReadableOrderRequestApproval readbleApprovers = new ReadableOrderRequestApproval();
				readbleApprovers.setApprovedBy(approvers.getApprovedBy());
				readbleApprovers.setApprovedNotes(approvers.getApprovedNotes());
				readbleApprovers.setApprovedTime(approvers.getApprovedTime());
				readbleApprovers.setId(approvers.getId());
				readbleApprovers.setOrders(approvers.getOrders());
				readbleApprovers.setStatus(approvers.getStatus());
				
				readbleApprover.add(readbleApprovers);
			}
			readableOrderRequest.setListOrderRequestApproval(readbleApprover);
			readableOrderRequest.setOrderId(or.getOrder().getId());
			
			listReadable.add(readableOrderRequest);
		}

		return ResponseEntity.ok(listReadable);
	}
	
	@PatchMapping("/private/orders/requests/{id}/accept")
	public ResponseEntity<?> acceptOrderRequest(
	        @PathVariable Long id,
	        @RequestParam String approverEmail) {

	    // Tim OrderRequest từ DB
	    OrderRequest orderRequest = orderRequestService.getById(id);
	    if (orderRequest == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                             .body("Order request not found");
	    }

	    // Tìm approver đang duyệt (status = PENDING)
	    List<OrderRequestApproval> approvals = orderRequest.getListOrderRequestApproval();

	    OrderRequestApproval currentApprover = approvals.stream()
	            .filter(a -> approverEmail.equalsIgnoreCase(a.getApprovedBy()))
	            .findFirst()
	            .orElse(null);

	    if (currentApprover == null) {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN)
	                             .body("Approver not found in this order request");
	    }

	    // Chỉ cho phép duyệt nếu status hiện tại là PENDING
	    if (currentApprover.getStatus() != RequestApprovalStatus.PENDING) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                             .body("This approver is not in PENDING status");
	    }

	    // Cập nhật trạng thái người hiện tại sang ACCEPTED
	    currentApprover.setStatus(RequestApprovalStatus.ACCEPTED);
	    currentApprover.setApprovedTime(LocalDateTime.now());

	    // Tìm người tiếp theo theo thứ tự `orders`
	    approvals.stream()
	            .filter(a -> a.getOrders() == currentApprover.getOrders() + 1)
	            .findFirst()
	            .ifPresent(next -> next.setStatus(RequestApprovalStatus.PENDING));

	    orderRequestService.save(orderRequest);

	    return ResponseEntity.ok("Update Status Successfully!");
	}
	
	
	
}
