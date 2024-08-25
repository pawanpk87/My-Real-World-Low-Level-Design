package org.buildcode.customer_invoice.exception;

import org.buildcode.customer_invoice.api.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomerInvoiceServiceExceptionHandler {

    @ExceptionHandler(CustomerInvoiceException.class)
    public ResponseEntity<ErrorResponse> handleCustomerInvoiceException(CustomerInvoiceException exception) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setResCode(exception.getResponseCode());
        errorResponse.setErrorCode(exception.getErrorCode());
        errorResponse.setMessage( exception.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(exception.getResponseCode()));
    }
}