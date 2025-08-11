package com.salesmanager.shop.model.order.orderrequest;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.salesmanager.core.model.order.orderrequest.OrderRequestApproval;
import com.salesmanager.core.model.user.User;

public class ReadableOrderRequest implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String code;
	private User createdBy;
	private long orderId;
	private LocalDateTime createdAt;
    private List<ReadableOrderRequestApproval> listOrderRequestApproval;
    private Long configId;
    
	public Long getConfigId() {
		return configId;
	}
	public void setConfigId(Long configId) {
		this.configId = configId;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
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
	
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	public List<ReadableOrderRequestApproval> getListOrderRequestApproval() {
		return listOrderRequestApproval;
	}
	public void setListOrderRequestApproval(List<ReadableOrderRequestApproval> listOrderRequestApproval) {
		this.listOrderRequestApproval = listOrderRequestApproval;
	}
	public long getOrderId() {
		return orderId;
	}
	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}
    
    

}
