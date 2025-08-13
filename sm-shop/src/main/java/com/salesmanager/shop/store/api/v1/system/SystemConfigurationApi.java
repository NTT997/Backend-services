package com.salesmanager.shop.store.api.v1.system;

import java.util.List;

import javax.inject.Inject;

import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.business.services.system.SystemConfigurationService;
import com.salesmanager.core.model.system.SystemConfigApprover;
import com.salesmanager.core.model.system.SystemConfiguration;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;

@RestController
@RequestMapping("/api/v1/system/configuration")
@Api(tags = { "System configuration management resource (System Config Management Api)" })
@SwaggerDefinition(tags = { @Tag(name = "System Config management resource", description = "Manage System Config by admmin") })
public class SystemConfigurationApi {
	
	@Inject
	private SystemConfigurationService systemConfigService;
	
    /**
     * Get list of all system configurations
     */
    @GetMapping
    @ApiOperation(value = "Get list of system configurations", notes = "Returns all system configuration entries")
    public ResponseEntity<List<SystemConfiguration>> getAllConfigurations() {
        List<SystemConfiguration> configs = systemConfigService.list();
        return ResponseEntity.ok(configs);
    }
    
    /**
     * Create a new system configuration
     */
    @PostMapping
    @ApiOperation(value = "Create a new system configuration", notes = "Adds a new configuration to the system")
    public ResponseEntity<SystemConfiguration> createNewConfiguration(@RequestBody SystemConfiguration config) {
        try {
            SystemConfiguration newSystemConfig = new SystemConfiguration();
            newSystemConfig.setKey(config.getKey());
            newSystemConfig.setTotalApprovers(config.getTotalApprovers());
            newSystemConfig.setValue(config.getValue());
            newSystemConfig.setMin(config.getMin());
            newSystemConfig.setMax(config.getMax());

            if (config.getApprovers() != null) {
                for (SystemConfigApprover approver : config.getApprovers()) {
                    approver.setSystemConfig(newSystemConfig); 
                }
                newSystemConfig.setApprovers(config.getApprovers());
            }

            systemConfigService.save(newSystemConfig);
            return ResponseEntity.status(HttpStatus.CREATED).body(newSystemConfig);
        } catch (ServiceException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PutMapping("/{id}")
    @ApiOperation(value = "Update a system configuration", notes = "Update a configuration to the system")
    public ResponseEntity<SystemConfiguration> updateConfiguration(@RequestBody SystemConfiguration config, @PathVariable Long id) {
        try {
            if (id == null) {
                return ResponseEntity.badRequest().build();
            }
        	
            SystemConfiguration systemConfig = systemConfigService.getById(config.getId());
            
            if(systemConfig == null) {
                return ResponseEntity.notFound().build();
            }
            
            systemConfig.setKey(config.getKey());
            systemConfig.setTotalApprovers(config.getTotalApprovers());
            systemConfig.setValue(config.getValue());
            systemConfig.setMin(config.getMin());
            systemConfig.setMax(config.getMax());

            if (config.getApprovers() != null) {
                for (SystemConfigApprover approver : config.getApprovers()) {
                    approver.setSystemConfig(systemConfig); 
                }
                systemConfig.setApprovers(config.getApprovers());
            }

            systemConfigService.save(systemConfig);
            return ResponseEntity.ok(systemConfig);
        } catch (ServiceException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSystemConfiguration(@PathVariable Long id) {
        SystemConfiguration sysConfig = systemConfigService.getById(id);
        if (sysConfig == null) {
            return ResponseEntity.notFound().build(); 
        }

        try {
			systemConfigService.delete(sysConfig);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
        return ResponseEntity.ok().body("Deleted config with id: " + id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSystemConfiguration(@PathVariable Long id) {
        SystemConfiguration sysConfig = systemConfigService.getById(id);
        if (sysConfig == null) {
            return ResponseEntity.notFound().build(); 
        }
        return ResponseEntity.ok().body(sysConfig);
    }
    
    
	//	private SystemConfigurationServiceImpl 

}
