package com.salesmanager.shop.model.catalog.product.product;

import java.io.Serializable;
import java.util.Date;

import com.salesmanager.shop.model.catalog.product.PersistableProductPrice;

public class PersistableProductInventory implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id; //huy
	private Date expiration;
	private String sku;
	private int quantity = 0;
	private PersistableProductPrice price;
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public PersistableProductPrice getPrice() {
		return price;
	}
	public void setPrice(PersistableProductPrice price) {
		this.price = price;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getExpiration() {
		return expiration;
	}
	public void setExpiration(Date expiration) {
		this.expiration = expiration;
	}

}
