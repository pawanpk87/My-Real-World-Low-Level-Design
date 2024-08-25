package org.buildcode.customer_invoice.exception;

public class CustomerInvoiceInternalServerException extends CustomerInvoiceException {

    public CustomerInvoiceInternalServerException(String message) {
        super(500, "INTERNAL_SERVER_ERROR", message);
    }
}
