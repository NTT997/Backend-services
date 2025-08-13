package com.salesmanager.shop.store.api.v1.system;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.salesmanager.core.business.services.system.SystemConfigurationService;
import com.salesmanager.core.model.system.SystemConfiguration;
import com.salesmanager.shop.model.system.ParameterConfiguration;


// @RestController
// @RequestMapping("/api/v1/configuration")
// public class ParameterTable {
//     @Autowired 
//     SystemConfigurationService systemConfigurationService;
    
//     @PostMapping("/config")
//     public SystemConfiguration updateConfiguration(@Valid @RequestBody ParameterConfiguration parameterTable,HttpServletRequest request) throws com.salesmanager.core.business.exception.ServiceException {
//         System.out.println(parameterTable);
//         return systemConfigurationService.updateByKey(parameterTable.getSystemConfigId(),parameterTable.getConfigValue());
//     }
//     @GetMapping("/configs")
//     public List<SystemConfiguration> getAllConfigurations() throws com.salesmanager.core.business.exception.ServiceException {
//         return systemConfigurationService.getAllConfigurations();
//     }
//     @GetMapping("/configs")
//     public List<SystemConfiguration> runScheduleTasks() throws com.salesmanager.core.business.exception.ServiceException {
//         return systemConfigurationService.getAllConfigurations();
//     }
// }
