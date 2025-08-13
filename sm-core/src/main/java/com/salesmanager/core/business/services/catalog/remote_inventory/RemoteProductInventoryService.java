package com.salesmanager.core.business.services.catalog.remote_inventory;

import java.util.List;

import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.model.catalog.product.availability.ProductAvailability;

public interface RemoteProductInventoryService {
    public void syncDataFromLocalToRemoteService(List<ProductAvailability>productAvailabilityList) throws ServiceException;
}
