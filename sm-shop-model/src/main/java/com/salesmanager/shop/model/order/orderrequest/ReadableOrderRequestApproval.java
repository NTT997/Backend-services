package com.salesmanager.shop.model.order.orderrequest;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.salesmanager.core.model.order.orderrequest.RequestApprovalStatus;

public class ReadableOrderRequestApproval implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private String approvedBy; //email
	private int orders; // thu tu duyet
	private RequestApprovalStatus status;
	private LocalDateTime approvedTime;
	private String approvedNotes;
	public long getId() {
		return id;
	}
	public void setId(long id) {
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
	
	

}
