package com.salesmanager.shop.store.api.exception;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice({ "com.salesmanager.shop.store.api" })
public class ResourceConflictErrorHandler extends RestErrorHandler{

	private static final Logger log = LoggerFactory.getLogger(ResourceConflictErrorHandler.class);

	@RequestMapping(produces = "application/json")
	@ExceptionHandler(ResourceAlreadyExistsException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public @ResponseBody ErrorEntity handleResourceAlreadyExists(ResourceAlreadyExistsException exception) {
	    log.error(exception.getErrorMessage(), exception);

	    return createErrorEntity(
	        exception.getErrorCode() != null ? exception.getErrorCode() : "409",
	        exception.getErrorMessage(),
	        exception.getLocalizedMessage()
	    );
	}
	
	private ErrorEntity createErrorEntity(String errorCode, String message, String detailMessage) {
        ErrorEntity errorEntity = new ErrorEntity();
        Optional.ofNullable(errorCode)
                .ifPresent(errorEntity::setErrorCode);

        String resultMessage = (message != null && detailMessage !=null)  ? new StringBuilder().append(message).append(", ").append(detailMessage).toString() : detailMessage;
        if(StringUtils.isBlank(resultMessage)) {
        	resultMessage = message;
        }
        Optional.ofNullable(resultMessage)
                .ifPresent(errorEntity::setMessage);
        return errorEntity;
    }
}