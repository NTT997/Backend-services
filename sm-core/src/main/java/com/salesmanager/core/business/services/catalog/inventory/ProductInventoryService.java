package com.salesmanager.core.business.services.catalog.inventory;

import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.model.catalog.product.Product;
import com.salesmanager.core.model.catalog.product.availability.ProductAvailability;
import com.salesmanager.core.model.catalog.product.inventory.ProductInventory;
import com.salesmanager.core.model.catalog.product.variant.ProductVariant;
import java.util.List;

public interface ProductInventoryService {
	
	
	ProductInventory inventory(Product product) throws ServiceException;
	ProductInventory inventory(ProductVariant variant) throws ServiceException;
	List<ProductAvailability>findAllProductInventory();
}
