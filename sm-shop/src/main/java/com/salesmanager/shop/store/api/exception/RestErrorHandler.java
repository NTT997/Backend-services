package com.salesmanager.shop.store.api.exception;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Order(Ordered.HIGHEST_PRECEDENCE)
//@ControllerAdvice({"com.salesmanager.shop.store.api"})
@RestControllerAdvice
public class RestErrorHandler {
  
    private static final Logger log = LoggerFactory.getLogger(RestErrorHandler.class);

    @RequestMapping(produces = "application/json")
    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody ErrorEntity handleServiceException(Exception exception) {
        log.error(exception.getMessage(), exception);
        Objects.requireNonNull(exception.getCause());
        Throwable rootCause = exception.getCause();
        while (rootCause.getCause() != null && rootCause.getCause() != rootCause) {
            rootCause = rootCause.getCause();
        }
        ErrorEntity errorEntity = createErrorEntity("500",exception.getMessage(),
        		rootCause.getMessage());
        return errorEntity;
    }

    /**
     * Generic exception serviceException handler
     */
    //OLD VERSION
    // @RequestMapping(produces = "application/json")
    // @ExceptionHandler(value = ServiceRuntimeException.class)
    // @ResponseStatus(HttpStatus.BAD_REQUEST)
    // public @ResponseBody ErrorEntity handleServiceException(ServiceRuntimeException exception) {
    //     log.error(exception.getErrorMessage(), exception);
    //     //Throwable rootCause = exception.getCause();//old code bug
    //     while (rootCause.getCause() != null && rootCause.getCause() != rootCause) {
    //         rootCause = rootCause.getCause();
    //     }
        
    //     System.err.println(rootCause);
    //     ErrorEntity errorEntity = createErrorEntity(exception.getErrorCode()!=null?exception.getErrorCode():"500", exception.getErrorMessage(),
    //     		rootCause.getMessage());
    //     return errorEntity;
    // }
    @RequestMapping(produces = "application/json")
    @ExceptionHandler(value = ServiceRuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody ErrorEntity handleServiceException(ServiceRuntimeException exception) {
        log.error(exception.getErrorMessage(), exception);
        Throwable rootCause = exception;
        while (rootCause.getCause() != null && rootCause.getCause() != rootCause) {
            rootCause = rootCause.getCause();
        }
        
        System.err.println(rootCause);
        ErrorEntity errorEntity = createErrorEntity(exception.getErrorCode()!=null?exception.getErrorCode():"500", exception.getErrorMessage(),
        		rootCause.getMessage());
        return errorEntity;
    }
    @RequestMapping(produces = "application/json")
    @ExceptionHandler(value = ConversionRuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody ErrorEntity handleServiceException(ConversionRuntimeException exception) {
        log.error(exception.getErrorMessage(), exception);
        ErrorEntity errorEntity = createErrorEntity(exception.getErrorCode()!=null?exception.getErrorCode():"400", exception.getErrorMessage(),
            exception.getLocalizedMessage());
        return errorEntity;
    }

    @RequestMapping(produces = "application/json")
    @ExceptionHandler(value = ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public @ResponseBody ErrorEntity handleServiceException(ResourceNotFoundException exception) {
        log.error(exception.getErrorMessage(), exception);

        ErrorEntity errorEntity = createErrorEntity(exception.getErrorCode(), exception.getErrorMessage(),
                exception.getLocalizedMessage());
        return errorEntity;
    }
    
    @RequestMapping(produces = "application/json")
    @ExceptionHandler(value = UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public @ResponseBody ErrorEntity handleServiceException(UnauthorizedException exception) {
        log.error(exception.getErrorMessage(), exception);

        ErrorEntity errorEntity = createErrorEntity(exception.getErrorCode(), exception.getErrorMessage(),
                exception.getLocalizedMessage());
        return errorEntity;
    }

    //new exception for error handling request
    @RequestMapping(produces = "application/json")
    @ExceptionHandler(value = GenericRuntimeException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public @ResponseBody ErrorEntity handleGenericRuntimeExceptionException(GenericRuntimeException exception) {
        log.error(exception.getErrorMessage(), exception);

        ErrorEntity errorEntity = createErrorEntity(exception.getErrorCode(), exception.getErrorMessage(),
                exception.getLocalizedMessage());
        return errorEntity;
    }

    @RequestMapping(produces = "application/json")
    @ExceptionHandler(value = RestApiException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody ErrorEntity handleRestApiException(RestApiException exception) {
        log.error(exception.getErrorMessage(), exception);

        ErrorEntity errorEntity = createErrorEntity(exception.getErrorCode(), exception.getErrorMessage(),
                exception.getLocalizedMessage());
        return errorEntity;
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
