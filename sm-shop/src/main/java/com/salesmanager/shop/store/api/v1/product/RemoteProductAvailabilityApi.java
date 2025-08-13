package com.salesmanager.shop.store.api.v1.product;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.salesmanager.core.model.catalog.product.availability.ProductAvailability;
import com.salesmanager.core.model.catalog.product.remote_availability.RemoteProductAvailability;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.reference.language.Language;
import com.salesmanager.shop.model.catalog.product.inventory.PersistableInventory;
import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.business.services.catalog.remote_inventory.RemoteProductInventoryService;

import java.sql.SQLException;
import java.util.List;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import springfox.documentation.annotations.ApiIgnore;

@Controller
@RequestMapping("/api/v1")
public class RemoteProductAvailabilityApi {
	
	@Autowired
	private RemoteProductInventoryService remoteProductInventoryService;
	
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = { "/public/remote-availability/sync-data" }, method = RequestMethod.POST)
	@ApiImplicitParams({ @ApiImplicitParam(name = "store", dataType = "String", defaultValue = "DEFAULT"),
			@ApiImplicitParam(name = "lang", dataType = "String", defaultValue = "en") })
	public void update(
			@Valid @RequestBody List<ProductAvailability>localProductAvailability,
			@ApiIgnore Language language) throws ServiceException, SQLException {
		//remoteProductInventoryService.syncDataFromLocalToRemoteService(localProductAvailability);
		remoteProductInventoryService.syncDataFromLocalToRemoteServiceSQL();
	}
}
