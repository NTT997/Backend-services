package com.salesmanager.core.business.services.catalog.remote_inventory;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.business.repositories.catalog.product.availability.ProductAvailabilityRepository;
import com.salesmanager.core.business.repositories.catalog.product.remote_availability.RemoteProductAvailabilityRepository;
import com.salesmanager.core.business.services.catalog.inventory.ProductInventoryService;
import com.salesmanager.core.business.services.sql.SqlUtilities;
import com.salesmanager.core.model.catalog.product.availability.ProductAvailability;
import com.salesmanager.core.model.catalog.product.remote_availability.RemoteProductAvailability;

@Service
public class RemoteProductInventoryServiceImpl implements RemoteProductInventoryService{

    @Autowired
    RemoteProductAvailabilityRepository remoteProductAvailabilityRepository;
    
    @Autowired
    private ProductAvailabilityRepository localProductAvailabilityRepository;
    
    @Override
    public void syncDataFromLocalToRemoteService(List<ProductAvailability> productAvailabilityList)
            throws ServiceException {
    	List<ProductAvailability>productAvailabilities = localProductAvailabilityRepository.findAll();

        
        if (productAvailabilities == null || productAvailabilities.isEmpty()) {
            // No data to sync
        	System.out.println("NO PRODUCT FOUND");
            return;
        }
        System.out.println("PRINT LINE 27 OF SYNC METHOD");
        for (ProductAvailability localProduct : productAvailabilities) {
            try {
            	System.out.println(localProduct.getId());
                
                RemoteProductAvailability remoteProduct = remoteProductAvailabilityRepository.getById(localProduct.getId());
                System.out.println(remoteProduct);
                
                if (remoteProduct == null) {
                	System.out.println("HANDLE WHEN REMOTE PRODUCT IS NULL");
                    RemoteProductAvailability remoteAvaiProduct = new RemoteProductAvailability(localProduct.getId(),localProduct.getProductQuantity(),localProduct.getProduct().getId());
                    
                    remoteProductAvailabilityRepository.save(remoteAvaiProduct);
                    continue;
                }

                
                if (localProduct.getProductQuantity() < remoteProduct.getProductQuantity()) {
                    remoteProduct.setProductQuantity(localProduct.getProductQuantity());
                    remoteProductAvailabilityRepository.save(remoteProduct); // Lưu lại
                    System.out.printf("Updated remote product {} quantity from {} to {}",
                            remoteProduct.getId(),
                            remoteProduct.getProductQuantity(),
                            localProduct.getProductQuantity());
                }

            } catch (Exception e) {
                System.out.printf("Error syncing product ID: {}", localProduct.getId(), e);
                throw new ServiceException("Failed to sync product ID: " + localProduct.getId(), e);
            }
        }
        
 
    }
	@Override
	public void syncDataFromLocalToRemoteServiceSQL() throws SQLException {
		SqlUtilities.syncDataFromLocalToRemote();	
	}

    
}
