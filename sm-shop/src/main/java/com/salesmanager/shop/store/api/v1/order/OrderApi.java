package com.salesmanager.shop.store.api.v1.order;

import java.net.http.HttpRequest;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.salesmanager.core.business.services.order.OrderService;
import com.salesmanager.core.model.order.orderstatus.OrderStatus;
import com.salesmanager.core.model.order.orderstatus.OrderStatusRequest;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.helper.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.business.repositories.order.OrderRepository;
import com.salesmanager.core.business.services.customer.CustomerService;
import com.salesmanager.core.business.services.shoppingcart.ShoppingCartService;
import com.salesmanager.core.business.services.user.UserService;
import com.salesmanager.core.model.customer.Customer;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.order.Order;
import com.salesmanager.core.model.order.OrderCriteria;
import com.salesmanager.core.model.order.OrderList;
import com.salesmanager.core.model.reference.language.Language;
import com.salesmanager.core.model.shoppingcart.ShoppingCart;
import com.salesmanager.core.model.user.User;
import com.salesmanager.shop.constants.Constants;
import com.salesmanager.shop.model.customer.PersistableCustomer;
import com.salesmanager.shop.model.customer.ReadableCustomer;
import com.salesmanager.shop.model.order.ReadableOrderV2;
import com.salesmanager.shop.model.order.v0.ReadableOrder;
import com.salesmanager.shop.model.order.v0.ReadableOrderList;
import com.salesmanager.shop.model.order.v1.PersistableAnonymousOrder;
import com.salesmanager.shop.model.order.v1.PersistableOrder;
import com.salesmanager.shop.model.order.v1.ReadableOrderConfirmation;
import com.salesmanager.shop.populator.customer.ReadableCustomerPopulator;
import com.salesmanager.shop.store.api.exception.GenericRuntimeException;
import com.salesmanager.shop.store.api.exception.ResourceNotFoundException;
import com.salesmanager.shop.store.api.exception.ServiceRuntimeException;
import com.salesmanager.shop.store.controller.customer.facade.CustomerFacade;
import com.salesmanager.shop.store.controller.order.facade.OrderFacade;
import com.salesmanager.shop.store.security.services.CredentialsException;
import com.salesmanager.shop.store.security.services.CredentialsService;
import com.salesmanager.shop.utils.AuthorizationUtils;
import com.salesmanager.shop.utils.LocaleUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/api/v1")
@Api(tags = { "Ordering api (Order Flow Api)" })
@SwaggerDefinition(tags = { @Tag(name = "Order flow resource", description = "Manage orders (create, list, get)") })
public class OrderApi {

	private static final Logger LOGGER = LoggerFactory.getLogger(OrderApi.class);

	@Inject
	private CustomerService customerService;

	@Inject
	private UserService userService;

	@Inject
	private OrderFacade orderFacade;

	@Inject
	private OrderService orderService;

	@Inject
	private com.salesmanager.shop.store.controller.order.facade.v1.OrderFacade orderFacadeV1;

	@Inject
	private ShoppingCartService shoppingCartService;

	@Autowired
	private CustomerFacade customerFacade;

	@Autowired
	private CustomerFacade customerFacadev1; // v1 version

	@Inject
	private AuthorizationUtils authorizationUtils;

	@Inject
	private CredentialsService credentialsService;

	@Inject
	private OrderRepository orderRepository;

	private static final String DEFAULT_ORDER_LIST_COUNT = "25";

	/**
	 * Get a list of orders for a given customer accept request parameter 'start'
	 * start index for count accept request parameter 'max' maximum number count,
	 * otherwise returns all Used for administrators
	 *
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = { "/private/orders/customers/{id}" }, method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@ApiImplicitParams({ @ApiImplicitParam(name = "store", dataType = "string", defaultValue = "DEFAULT"),
			@ApiImplicitParam(name = "lang", dataType = "string", defaultValue = "en") })
	public ReadableOrderList list(@PathVariable final Long id,
			@RequestParam(value = "start", required = false) Integer start,
			@RequestParam(value = "count", required = false) Integer count, @ApiIgnore MerchantStore merchantStore,
			@ApiIgnore Language language, HttpServletResponse response) throws Exception {

		Customer customer = customerService.getById(id);

		if (customer == null) {
			LOGGER.error("Customer is null for id " + id);
			response.sendError(404, "Customer is null for id " + id);
			return null;
		}

//		if (start == null) {
//			start = new Integer(0);
//		}
//		if (count == null) {
//			count = new Integer(100);
//		}

		if (start == null) {
			start = Integer.valueOf(0);
		}
		if (count == null) {
			count = Integer.valueOf(100);
		}

		ReadableCustomer readableCustomer = new ReadableCustomer();
		ReadableCustomerPopulator customerPopulator = new ReadableCustomerPopulator();
		customerPopulator.populate(customer, readableCustomer, merchantStore, language);

		ReadableOrderList returnList = orderFacade.getReadableOrderList(merchantStore, customer, start, count,
				language);

		List<ReadableOrder> orders = returnList.getOrders();

		if (!CollectionUtils.isEmpty(orders)) {
			for (ReadableOrder order : orders) {
				order.setCustomer(readableCustomer);
			}
		}

		return returnList;
	}

	/**
	 * List orders for authenticated customers
	 *
	 * @param start
	 * @param count
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = { "/auth/orders" }, method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@ApiImplicitParams({ @ApiImplicitParam(name = "store", dataType = "string", defaultValue = "DEFAULT"),
			@ApiImplicitParam(name = "lang", dataType = "string", defaultValue = "en") })
	public ReadableOrderList list(@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "count", required = false) Integer count, @ApiIgnore MerchantStore merchantStore,
			@ApiIgnore Language language, HttpServletRequest request, HttpServletResponse response) throws Exception {

		Principal principal = request.getUserPrincipal();
		String userName = principal.getName();

		Customer customer = customerService.getByNick(userName);

		if (customer == null) {
			response.sendError(401, "Error while listing orders, customer not authorized");
			return null;
		}

		if (page == null) {
			page = new Integer(0);
		}
		if (count == null) {
			count = new Integer(100);
		}

		ReadableCustomer readableCustomer = new ReadableCustomer();
		ReadableCustomerPopulator customerPopulator = new ReadableCustomerPopulator();
		customerPopulator.populate(customer, readableCustomer, merchantStore, language);

		ReadableOrderList returnList = orderFacade.getReadableOrderList(merchantStore, customer, page, count, language);

		if (returnList == null) {
			returnList = new ReadableOrderList();
		}

		List<ReadableOrder> orders = returnList.getOrders();
		if (!CollectionUtils.isEmpty(orders)) {
			for (ReadableOrder order : orders) {
				order.setCustomer(readableCustomer);
			}
		}
		return returnList;
	}

	/**
	 * This method returns list of all the orders for a store.This is not bound to
	 * any specific stores and will get list of all the orders available for this
	 * instance
	 *
	 * @param start
	 * @param count
	 * @return List of orders
	 * @throws Exception
	 */
	@RequestMapping(value = { "/private/orders" }, method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ReadableOrderList list(
			@RequestParam(value = "count", required = false, defaultValue = DEFAULT_ORDER_LIST_COUNT) Integer count,
			@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "status", required = false) String status,
			@RequestParam(value = "phone", required = false) String phone,
			@RequestParam(value = "email", required = false) String email,
			@RequestParam(value = "emailAdmin", required = false) String emailAdmin,

			@ApiIgnore MerchantStore merchantStore, @ApiIgnore Language language) {

		OrderCriteria orderCriteria = new OrderCriteria();
		orderCriteria.setPageSize(count);
		orderCriteria.setStartPage(page);

		orderCriteria.setCustomerName(name);
		orderCriteria.setCustomerPhone(phone);
		orderCriteria.setStatus(status);
		orderCriteria.setEmail(email);
		orderCriteria.setId(id);
		orderCriteria.setUser(emailAdmin);

//		String user = authorizationUtils.authenticatedUser();
//		authorizationUtils.authorizeUser(user, Stream.of(Constants.GROUP_SUPERADMIN, Constants.GROUP_ADMIN,
//				Constants.GROUP_ADMIN_ORDER, Constants.GROUP_ADMIN_RETAIL).collect(Collectors.toList()), merchantStore);
//		String user = authorizationUtils.authenticatedUser();
//		authorizationUtils.authorizeUser(user, Stream.of(Constants.GROUP_ADMIN,
//				Constants.GROUP_ADMIN_ORDER, Constants.GROUP_ADMIN_RETAIL).collect(Collectors.toList()), merchantStore);

		ReadableOrderList orders = orderFacade.getReadableOrderList(orderCriteria, merchantStore);

		return orders;

	}

	/**
	 * Order details
	 * 
	 * @param id
	 * @param merchantStore
	 * @param language
	 * @return
	 */
	@RequestMapping(value = { "/private/orders/{id}" }, method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@ApiImplicitParams({ @ApiImplicitParam(name = "store", dataType = "string", defaultValue = "DEFAULT"),
			@ApiImplicitParam(name = "lang", dataType = "string", defaultValue = "en") })
	public ReadableOrder get(@PathVariable final Long id, @ApiIgnore MerchantStore merchantStore,
			@ApiIgnore Language language) {

		String user = authorizationUtils.authenticatedUser();
		authorizationUtils.authorizeUser(user, Stream.of(Constants.GROUP_SUPERADMIN, Constants.GROUP_ADMIN,
				Constants.GROUP_ADMIN_ORDER, Constants.GROUP_ADMIN_RETAIL).collect(Collectors.toList()), merchantStore);

		ReadableOrder order = orderFacade.getReadableOrder(id, merchantStore, language);

		return order;
	}

	/**
	 * Get a given order by id
	 *
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = { "/auth/orders/{id}" }, method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@ApiImplicitParams({ @ApiImplicitParam(name = "store", dataType = "string", defaultValue = "DEFAULT"),
			@ApiImplicitParam(name = "lang", dataType = "string", defaultValue = "en") })
	public ReadableOrder getOrder(@PathVariable final Long id, @ApiIgnore MerchantStore merchantStore,
			@ApiIgnore Language language, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Principal principal = request.getUserPrincipal();
		String userName = principal.getName();

		Customer customer = customerService.getByNick(userName);

		if (customer == null) {
			response.sendError(401, "Error while performing checkout customer not authorized");
			return null;
		}

		ReadableOrder order = orderFacade.getReadableOrder(id, merchantStore, language);

		if (order == null) {
			LOGGER.error("Order is null for id " + id);
			response.sendError(404, "Order is null for id " + id);
			return null;
		}

		if (order.getCustomer() == null) {
			LOGGER.error("Order is null for customer " + principal);
			response.sendError(404, "Order is null for customer " + principal);
			return null;
		}

		if (order.getCustomer().getId() != null
				&& order.getCustomer().getId().longValue() != customer.getId().longValue()) {
			LOGGER.error("Order is null for customer " + principal);
			response.sendError(404, "Order is null for customer " + principal);
			return null;
		}

		return order;
	}

	/**
	 * Action for performing a checkout on a given shopping cart
	 *
	 * @param id
	 * @param order
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = { "/auth/cart/{code}/checkout" }, method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@ApiImplicitParams({ @ApiImplicitParam(name = "store", dataType = "string", defaultValue = "DEFAULT"),
			@ApiImplicitParam(name = "lang", dataType = "string", defaultValue = "en") })
	public ReadableOrderConfirmation checkout(@PathVariable final String code, // shopping cart
			@Valid @RequestBody PersistableOrder order, // order
			@ApiIgnore MerchantStore merchantStore, @ApiIgnore Language language, HttpServletRequest request,
			HttpServletResponse response, Locale locale) throws Exception {

		try {
			Principal principal = request.getUserPrincipal();
			String userName = principal.getName();

			Customer customer = customerService.getByNick(userName);

			if (customer == null) {
				response.sendError(401, "Error while performing checkout customer not authorized");
				return null;
			}

			ShoppingCart cart = shoppingCartService.getByCode(code, merchantStore);
			if (cart == null) {
				throw new ResourceNotFoundException("Cart code " + code + " does not exist");
			}

			order.setShoppingCartId(cart.getId());
			order.setCustomerId(customer.getId());// That is an existing customer purchasing

			Order modelOrder = orderFacade.processOrder(order, customer, merchantStore, language, locale, "");
			Long orderId = modelOrder.getId();
			modelOrder.setId(orderId);

			return orderFacadeV1.orderConfirmation(modelOrder, customer, merchantStore, language);

		} catch (Exception e) {
			LOGGER.error("Error while processing checkout", e);
			try {
				response.sendError(503, "Error while processing checkout " + e.getMessage());
			} catch (Exception ignore) {
			}
			return null;
		}
	}

	/**
	 * Action for performing a checkout on a given shopping cart
	 *
	 * @param id
	 * @param order
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = { "/private/cart/{code}/checkout" }, method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@ApiImplicitParams({ @ApiImplicitParam(name = "store", dataType = "string", defaultValue = "DEFAULT"),
			@ApiImplicitParam(name = "lang", dataType = "string", defaultValue = "en") })
	public ReadableOrderConfirmation privateCheckout(@PathVariable final String code, // shopping cart
			@Valid @RequestBody PersistableOrder order, // order
			@ApiIgnore MerchantStore merchantStore, @ApiIgnore Language language, HttpServletRequest request,
			HttpServletResponse response, Locale locale) throws Exception {

		try {
			Principal principal = request.getUserPrincipal();
			String userName = principal.getName();

			User user = userService.getByUserName(userName);

			if (user == null) {
				response.sendError(401, "Error while performing checkout private admin not authorized");
				return null;
			}

			Customer customer = customerService.getById(order.getCustomerId());
			if (customer == null) {
				System.out.println("customer id: " + order.getCustomerId().toString());
				response.sendError(400, "Cant find any customer");
				return null;
			}

			ShoppingCart cart = shoppingCartService.getByCode(code, merchantStore);
			if (cart == null) {
				throw new ResourceNotFoundException("Cart code " + code + " does not exist");
			}

			order.setShoppingCartId(cart.getId());

			Order modelOrder = orderFacade.processOrder(order, customer, merchantStore, language, locale,
					request.getUserPrincipal().getName());
			Long orderId = modelOrder.getId();
			modelOrder.setId(orderId);

			return orderFacadeV1.orderConfirmation(modelOrder, customer, merchantStore, language);

		} catch (Exception e) {
			LOGGER.error("Error while processing checkout", e);
			try {
				response.sendError(503, "Error while processing checkout " + e.getMessage());
			} catch (Exception ignore) {
			}
			return null;
		}
	}

	/**
	 * Main checkout resource that will complete the order flow
	 * 
	 * @param code
	 * @param order
	 * @param merchantStore
	 * @param language
	 * @return
	 */
	@RequestMapping(value = { "/cart/{shoppingCartCode}/checkout" }, method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@ApiImplicitParams({ @ApiImplicitParam(name = "store", dataType = "string", defaultValue = "DEFAULT"),
			@ApiImplicitParam(name = "lang", dataType = "string", defaultValue = "en") })
	public ReadableOrderConfirmation checkout(@PathVariable final String shoppingCartCode, // shopping cart
			@Valid @RequestBody PersistableAnonymousOrder order, // order
			@ApiIgnore MerchantStore merchantStore, @ApiIgnore Language language) {

		Validate.notNull(order.getCustomer(), "Customer must not be null");

		ShoppingCart cart;
		try {
			cart = shoppingCartService.getByCode(shoppingCartCode, merchantStore);

			if (cart == null) {
				throw new ResourceNotFoundException("Cart code " + shoppingCartCode + " does not exist");
			}

			// security password validation
			PersistableCustomer presistableCustomer = order.getCustomer();
			if (!StringUtils.isBlank(presistableCustomer.getPassword())) { // validate customer password
				credentialsService.validateCredentials(presistableCustomer.getPassword(),
						presistableCustomer.getRepeatPassword(), merchantStore, language);
			}

			Customer customer = new Customer();
			customer = customerFacade.populateCustomerModel(customer, order.getCustomer(), merchantStore, language);

			if (!StringUtils.isBlank(presistableCustomer.getPassword())) {
				// check if customer already exist
				customer.setAnonymous(false);
				customer.setNick(customer.getNick());
				customer.setEmailAddress(customer.getEmailAddress()); // username
				if (customerFacadev1.checkIfUserExists(customer.getNick(), merchantStore)) {
					// 409 Conflict
					throw new GenericRuntimeException("409",
							"Customer with email [" + customer.getEmailAddress() + "] is already registered");
				}
			}

			order.setShoppingCartId(cart.getId());

			Order modelOrder = orderFacade.processOrder(order, customer, merchantStore, language,
					LocaleUtils.getLocale(language), "");
			Long orderId = modelOrder.getId();
			// populate order confirmation
			order.setId(orderId);
			// set customer id
			order.getCustomer().setId(modelOrder.getCustomerId());

			return orderFacadeV1.orderConfirmation(modelOrder, customer, merchantStore, language);

		} catch (Exception e) {
			if (e instanceof CredentialsException) {
				throw new GenericRuntimeException("412", "Credentials creation Failed [" + e.getMessage() + "]");
			}
			String message = e.getMessage();
			if (StringUtils.isBlank(message)) {// exception type
				message = "APP-BACKEND";
				if (e.getCause() instanceof com.salesmanager.core.modules.integration.IntegrationException) {
					message = "Integration problen occured to complete order";
				}
			}
			throw new ServiceRuntimeException("Error during checkout [" + message + "]", e);
		}

	}

	@RequestMapping(value = { "/private/orders/{id}/customer" }, method = RequestMethod.PATCH)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@ApiImplicitParams({ @ApiImplicitParam(name = "store", dataType = "string", defaultValue = "DEFAULT"),
			@ApiImplicitParam(name = "lang", dataType = "string", defaultValue = "en") })
	public void updateOrderCustomer(@PathVariable final Long id, @Valid @RequestBody PersistableCustomer orderCustomer,
			@ApiIgnore MerchantStore merchantStore, @ApiIgnore Language language) {

//		String user = authorizationUtils.authenticatedUser();
//		authorizationUtils.authorizeUser(user, Stream.of(Constants.GROUP_SUPERADMIN, Constants.GROUP_ADMIN,
//				Constants.GROUP_ADMIN_ORDER, Constants.GROUP_ADMIN_RETAIL).collect(Collectors.toList()), merchantStore);

		orderFacade.updateOrderCustomre(id, orderCustomer, merchantStore);
		return;
	}

	@RequestMapping(value = { "/private/orders/{id}/status" }, method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@ApiImplicitParams({ @ApiImplicitParam(name = "store", dataType = "string", defaultValue = "DEFAULT"),
			@ApiImplicitParam(name = "lang", dataType = "string", defaultValue = "en") })
	public void updateOrderStatus(@PathVariable final Long id, @Valid @RequestBody OrderStatusRequest statusRequest,
//			@Valid @RequestBody String status,
			@ApiIgnore MerchantStore merchantStore, @ApiIgnore Language language) {

		String user = authorizationUtils.authenticatedUser();
		authorizationUtils.authorizeUser(user, Stream.of(Constants.GROUP_SUPERADMIN, Constants.GROUP_ADMIN,
				Constants.GROUP_ADMIN_ORDER, Constants.GROUP_ADMIN_RETAIL).collect(Collectors.toList()), merchantStore);

		Order order = orderService.getOrder(id, merchantStore);
		if (order == null) {
			throw new GenericRuntimeException("412", "Order not found [" + id + "]");
		}

//		OrderStatus statusEnum = OrderStatus.valueOf(status);
		OrderStatus statusEnum = OrderStatus.valueOf(statusRequest.getStatus());
		orderFacade.updateOrderStatus(order, statusEnum, merchantStore);
		return;
	}

	// resubmit lai khi bi approver reject
	@RequestMapping(value = { "/private/orders/{id}/resubmit" }, method = RequestMethod.PATCH)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@ApiImplicitParams({ @ApiImplicitParam(name = "store", dataType = "string", defaultValue = "DEFAULT"),
			@ApiImplicitParam(name = "lang", dataType = "string", defaultValue = "en") })
	public void resubmitOrderCustomer(@PathVariable final Long id,
			@Valid @RequestBody PersistableCustomer orderCustomer, @ApiIgnore MerchantStore merchantStore,
			@ApiIgnore Language language, HttpServletRequest req) throws ServiceException {

		String username = req.getUserPrincipal().getName();

		Order order = orderService.getById(id);
		if (order.getAuditSection().getModifiedBy() != username) {
			throw new ServiceException("You cant modify this order!");
		}

//		String user = authorizationUtils.authenticatedUser();
//		authorizationUtils.authorizeUser(user, Stream.of(Constants.GROUP_SUPERADMIN, Constants.GROUP_ADMIN,
//				Constants.GROUP_ADMIN_ORDER, Constants.GROUP_ADMIN_RETAIL).collect(Collectors.toList()), merchantStore);

		orderFacade.updateOrderCustomre(id, orderCustomer, merchantStore);
		return;
	}

	// get List Order co Order Request = REJECTED
	@RequestMapping(value = { "/private/orders/resubmit" }, method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@ApiImplicitParams({
	    @ApiImplicitParam(name = "store", dataType = "string", defaultValue = "DEFAULT"),
	    @ApiImplicitParam(name = "lang", dataType = "string", defaultValue = "en")
	})
	public List<ReadableOrderV2> getListOrderResubmitByAdmin(
	        @RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
	        @RequestParam(value = "size", defaultValue = "15", required = false) Integer size,
	        @ApiIgnore MerchantStore merchantStore, @ApiIgnore Language lang,
	        @RequestParam("email") String email) throws Exception {

//	    Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
//	    Page<Order> pageResult = orderRepository.findRejectedOrdersByEmail(email, pageable);
//
//	    OrderList orderList = new OrderList();
//	    orderList.setOrders(pageResult.getContent());
//	    orderList.setTotalCount((int) pageResult.getTotalElements());
//	    orderList.setTotalPages(pageResult.getTotalPages());
		List<Order> orders = orderService.getListOrderRejectByEmail(email);
		List<ReadableOrderV2> lstReadableOrder = new ArrayList<>();
		for (Order o : orders) {
			ReadableOrderV2 readableOrder = new ReadableOrderV2();
			readableOrder.setId(o.getId());
			readableOrder.setCustomerEmailAddress(o.getCustomerEmailAddress());
			readableOrder.setDatePurchased(o.getDatePurchased());
			readableOrder.setStatus(o.getStatus().getValue());
			readableOrder.setTotal(o.getTotal());
			readableOrder.setCreatedB(o.getAuditSection().getModifiedBy());
			
			lstReadableOrder.add(readableOrder);
		}

	    return lstReadableOrder; 
	}


}
