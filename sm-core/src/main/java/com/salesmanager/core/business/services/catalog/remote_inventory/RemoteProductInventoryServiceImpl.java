package com.salesmanager.core.business.services.catalog.remote_inventory;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.business.repositories.catalog.product.remote_availability.RemoteProductAvailabilityRepository;
import com.salesmanager.core.model.catalog.product.availability.ProductAvailability;
import com.salesmanager.core.model.catalog.product.remote_availability.RemoteProductAvailability;

@Service
public class RemoteProductInventoryServiceImpl implements RemoteProductInventoryService{

    @Autowired
    RemoteProductAvailabilityRepository remoteProductAvailabilityRepository;

    @Override
    public void syncDataFromLocalToRemoteService(List<ProductAvailability> productAvailabilityList)
            throws ServiceException {

        if (productAvailabilityList == null || productAvailabilityList.isEmpty()) {
            // No data to sync
            return;
        }

        for (ProductAvailability localProduct : productAvailabilityList) {
            try {
                // Lấy bản copy từ remote server
                RemoteProductAvailability remoteProduct = null;
                
                System.out.println(remoteProduct);
                long temp_data = 1;
                remoteProduct = remoteProductAvailabilityRepository.getById(temp_data);
                System.out.println(remoteProduct);
                
                if (remoteProduct == null) {
                    RemoteProductAvailability remoteAvaiProduct = new RemoteProductAvailability(localProduct.getId(),localProduct.getProductQuantity(),temp_data);
                    
                    remoteProductAvailabilityRepository.save(remoteAvaiProduct);
                    continue;
                }

                // So sánh số lượng và cập nhật nếu cần
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
    
}
