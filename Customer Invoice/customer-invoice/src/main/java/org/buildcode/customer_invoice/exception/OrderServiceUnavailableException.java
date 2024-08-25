package org.buildcode.customer_invoice.exception;

public class OrderServiceUnavailableException extends CustomerInvoiceException {

    public OrderServiceUnavailableException(String message) {
        super(500, "ORDER_SERVICE_UNAVAILABLE", message);
    }
}
