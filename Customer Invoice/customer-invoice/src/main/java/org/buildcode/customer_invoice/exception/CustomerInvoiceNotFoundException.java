package org.buildcode.customer_invoice.exception;

public class CustomerInvoiceNotFoundException extends CustomerInvoiceException {

    public CustomerInvoiceNotFoundException(String message) {
        super(404, "INVOICE_NOT_FOUND", message);
    }
}
