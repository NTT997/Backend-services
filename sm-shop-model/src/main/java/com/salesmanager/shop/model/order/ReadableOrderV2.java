package com.salesmanager.shop.model.order;

import java.math.BigDecimal;
import java.util.Date;

public class ReadableOrderV2 {
	private Long id;
	private String status;
	private Date datePurchased;
	private BigDecimal total;
	private String customerEmailAddress;
	private String createdBy;

	public ReadableOrderV2(Long id, String status, Date datePurchased, BigDecimal total, String customerEmailAddress) {
		this.id = id;
		this.status = status;
		this.datePurchased = datePurchased;
		this.total = total;
		this.customerEmailAddress = customerEmailAddress;
	}

	public ReadableOrderV2() {
	}
	
	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedB(String email) {
		this.createdBy = email;
	}


	// Getter & Setter
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getDatePurchased() {
		return datePurchased;
	}

	public void setDatePurchased(Date datePurchased) {
		this.datePurchased = datePurchased;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public String getCustomerEmailAddress() {
		return customerEmailAddress;
	}

	public void setCustomerEmailAddress(String customerEmailAddress) {
		this.customerEmailAddress = customerEmailAddress;
	}
}
