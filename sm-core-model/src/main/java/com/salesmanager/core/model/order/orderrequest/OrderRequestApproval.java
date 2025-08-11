package com.salesmanager.core.model.order.orderrequest;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.salesmanager.core.model.common.audit.AuditListener;
import com.salesmanager.core.model.common.audit.AuditSection;
import com.salesmanager.core.model.common.audit.Auditable;
import com.salesmanager.core.model.generic.SalesManagerEntity;

@Entity
@EntityListeners(value = AuditListener.class)
@Table(name = "ORDER_REQUEST_APPROVALS")
public class OrderRequestApproval extends SalesManagerEntity<Long, OrderRequestApproval> implements Auditable{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_request_id", nullable = false)
	private OrderRequest orderRequest;
    
	public OrderRequest getOrderRequest() {
		return orderRequest;
	}

	public void setOrderRequest(OrderRequest orderRequest) {
		this.orderRequest = orderRequest;
	}
	
	@Column(name = "APPROVED_BY")
	private String approvedBy; //email
	
	@Column(name = "ORDERS")
	private int orders; // thu tu duyet

	
	@Enumerated(EnumType.STRING)
	private RequestApprovalStatus status; //pending, accepted, closed, rejected
	
	@Column(name = "APPROVED_TIME")
	private LocalDateTime approvedTime;
	
	@Column(columnDefinition = "TEXT", name = " APPROVED_NOTES")
	private String approvedNotes;
	
    @Embedded private AuditSection auditSection = new AuditSection();
    
	@Override
	public AuditSection getAuditSection() {
		// TODO Auto-generated method stub
		return auditSection;
	}

	@Override
	public void setAuditSection(AuditSection audit) {
		// TODO Auto-generated method stub
		this.auditSection = audit;
	}

	@Override
	public Long getId() {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public void setId(Long id) {
		// TODO Auto-generated method stub
		this.id = id;
	}


	public String getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}

	public int getOrders() {
		return orders;
	}

	public void setOrders(int orders) {
		this.orders = orders;
	}

	public RequestApprovalStatus getStatus() {
		return status;
	}

	public void setStatus(RequestApprovalStatus status) {
		this.status = status;
	}

	public LocalDateTime getApprovedTime() {
		return approvedTime;
	}

	public void setApprovedTime(LocalDateTime approvedTime) {
		this.approvedTime = approvedTime;
	}

	public String getApprovedNotes() {
		return approvedNotes;
	}

	public void setApprovedNotes(String approvedNotes) {
		this.approvedNotes = approvedNotes;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}

}
