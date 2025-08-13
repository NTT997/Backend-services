package com.salesmanager.core.business.services.catalog.remote_inventory;

import java.sql.SQLException;
import java.util.List;

import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.model.catalog.product.availability.ProductAvailability;

public interface RemoteProductInventoryService {
    public void syncDataFromLocalToRemoteService(List<ProductAvailability>productAvailabilityList) throws ServiceException;
    public void syncDataFromLocalToRemoteServiceSQL() throws SQLException;
}
