package org.buildcode.customer_invoice.exception;

import lombok.Data;

@Data
public class CustomerInvoiceException extends RuntimeException {

    private final int responseCode;
    private final String errorCode;

    public CustomerInvoiceException(int responseCode, String errorCode, String message) {
        super(message);
        this.responseCode = responseCode;
        this.errorCode = errorCode;
    }
}