package com.salesmanager.shop.store.api.v1.payment;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.salesmanager.core.model.payments.PaymentType;

@RestController
@RequestMapping("/api/v1")
public class PaymentTypeApi {

	@GetMapping("/private/modules/payment/types")
	public List<Map<String, String>> getPaymentTypes() {
		return Arrays.stream(PaymentType.values()).map(pt -> {
			Map<String, String> map = new HashMap<>();
			map.put("name", pt.name()); // ACCOUNTCREDIT, CREDITCARD...
			map.put("value", pt.toString()); // creditcard, free...
			return map;
		}).collect(Collectors.toList());
	}

}
