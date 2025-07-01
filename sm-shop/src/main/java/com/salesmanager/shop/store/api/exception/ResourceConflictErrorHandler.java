package com.salesmanager.shop.store.api.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Order(Ordered.HIGHEST_PRECEDENCE + 1) // Runs after RestErrorHandler if both match
@ControllerAdvice({ "com.salesmanager.shop.store.api" })
public class ResourceConflictErrorHandler {

	private static final Logger log = LoggerFactory.getLogger(ResourceConflictErrorHandler.class);

	@RequestMapping(produces = "application/json")
	@ExceptionHandler(ResourceAlreadyExistsException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public @ResponseBody ErrorEntity handleResourceAlreadyExists(ResourceAlreadyExistsException exception) {
		log.error("Conflict error: {}", exception.getErrorMessage(), exception);

		ErrorEntity errorEntity = new ErrorEntity();
		errorEntity.setErrorCode(exception.getErrorCode());
		errorEntity.setMessage(exception.getErrorMessage());
		return errorEntity;
	}
}