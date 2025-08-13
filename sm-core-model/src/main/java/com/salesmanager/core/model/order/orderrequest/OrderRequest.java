package com.salesmanager.core.model.order.orderrequest;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.salesmanager.core.model.common.audit.AuditSection;
import com.salesmanager.core.model.common.audit.Auditable;
import com.salesmanager.core.model.generic.SalesManagerEntity;
import com.salesmanager.core.model.order.Order;
import com.salesmanager.core.model.system.SystemConfiguration;
import com.salesmanager.core.model.user.User;

@Entity
@Table(name = "ORDER_REQUEST")
public class OrderRequest extends SalesManagerEntity<Long, OrderRequest> implements Auditable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String code;
	
	@OneToOne
	@JoinColumn(name = "created_by")
	private User createdBy;

	@OneToOne
	@JoinColumn(name = "order_id")
	private Order order;
	
	@Column(name = "CREATED_AT")
    private LocalDateTime createdAt;
	
	@Enumerated(EnumType.STRING)
	private OrderRequestStatus status;
    
    @ManyToOne
    @JoinColumn(name = "system_config_id")
    @JsonIgnore
    private SystemConfiguration systemConfig;

	@OneToMany(mappedBy = "orderRequest", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER )
    private List<OrderRequestApproval> listOrderRequestApproval;
	
    
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public SystemConfiguration getSystemConfig() {
		return systemConfig;
	}

	public void setSystemConfig(SystemConfiguration systemConfig) {
		this.systemConfig = systemConfig;
	}

	public List<OrderRequestApproval> getListOrderRequestApproval() {
		return listOrderRequestApproval;
	}

	public void setListOrderRequestApproval(List<OrderRequestApproval> listOrderRequestApproval) {
		this.listOrderRequestApproval = listOrderRequestApproval;
	}
	
	public OrderRequestStatus getStatus() {
		return status;
	}

	public void setStatus(OrderRequestStatus status) {
		this.status = status;
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

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}

	private AuditSection audit = new AuditSection();
	
	@Override
	public AuditSection getAuditSection() {
		// TODO Auto-generated method stub
		return audit;
	}

	@Override
	public void setAuditSection(AuditSection audit) {
		// TODO Auto-generated method stub
		this.audit= audit;
	}
	
	
}
