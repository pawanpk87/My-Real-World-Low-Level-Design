package org.buildcode.customer_invoice.exception;

public class CustomerInvoiceGenerationException extends CustomerInvoiceException {

    public CustomerInvoiceGenerationException(String message) {
        super(400, "INVOICE_GENERATION_FAILED", message);
    }
}