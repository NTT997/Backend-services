package com.salesmanager.shop.store.api.v1.configurations;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.reference.language.Language;
import com.salesmanager.core.model.system.MerchantConfiguration;
import com.salesmanager.shop.model.configuration.ReadableConfiguration;
import com.salesmanager.shop.model.system.PersistableIntegrationConfiguration;
import com.salesmanager.shop.model.system.ReadableMerchantConfiguration;
import com.salesmanager.shop.store.controller.system.MerchantConfigurationFacade;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping(value = "/api/v1")
@Api(tags = { "Configurations management" })
@SwaggerDefinition(tags = {
		@Tag(name = "Configurations management", description = "Configurations management for modules") })
public class ConfigurationsApi {

	@Autowired
	private MerchantConfigurationFacade merchantConfigurationFacade;

	/** Configurations of modules */
	// api init confirguration payment
	@PostMapping("/private/configurations/payment")
	@ApiOperation(httpMethod = "POST", value = "Manages payment configurations", notes = "Requires administration access", produces = "application/json", response = Void.class)
	@ApiImplicitParams({ @ApiImplicitParam(name = "store", dataType = "string", defaultValue = "DEFAULT") })
	public void create(@RequestBody @Valid List<PersistableIntegrationConfiguration> configs,
			@ApiIgnore MerchantStore merchantStore, @ApiIgnore Language language) {

		merchantConfigurationFacade.saveConfiguratation(configs, merchantStore, language);
	}

//	/** Configurations of payment modules */
//	@GetMapping("/private/configurations/payment")
//	@ApiOperation(httpMethod = "GET", value = "List payment configurations summary", notes = "Requires administration access", produces = "application/json", response = List.class)
//	@ApiImplicitParams({ @ApiImplicitParam(name = "store", dataType = "string", defaultValue = "DEFAULT") })
//	public List<ReadableConfiguration> listPaymentConfigurations(@ApiIgnore MerchantStore merchantStore,
//			@ApiIgnore Language language) {
//		return merchantConfigurationFacade.getListPaymentConfig(merchantStore, language);
//
//	}
	
	/** Configurations of payment modules */
	@GetMapping("/private/configurations/payment")
	@ApiOperation(httpMethod = "GET", value = "List payment configurations summary", notes = "Requires administration access", produces = "application/json", response = List.class)
	@ApiImplicitParams({ @ApiImplicitParam(name = "store", dataType = "string", defaultValue = "DEFAULT") })
	public ReadableMerchantConfiguration listPaymentConfigurations(@ApiIgnore MerchantStore merchantStore,
			@ApiIgnore Language language) {
		return merchantConfigurationFacade.getListPaymentConfig(merchantStore, language);

	}

	/** Configurations of shipping modules */
	@GetMapping("/private/configurations/shipping")
	@ApiOperation(httpMethod = "GET", value = "List shipping configurations summary", notes = "Requires administration access", produces = "application/json", response = List.class)
	@ApiImplicitParams({ @ApiImplicitParam(name = "store", dataType = "string", defaultValue = "DEFAULT") })
	public List<ReadableConfiguration> listShippingConfigurations(@ApiIgnore MerchantStore merchantStore,
			@ApiIgnore Language language) {
		// return customerFacade.create(customer, merchantStore, language);
		return null;

	}

}
