package org.buildcode.customer_invoice.data.dto;

import lombok.Data;
import org.buildcode.customer_invoice.api.constants.Address;
import org.buildcode.customer_invoice.api.constants.InvoiceStatus;
import org.buildcode.customer_invoice.api.constants.InvoiceType;

import java.time.Instant;

@Data
public class InvoiceDto {
    private String id;
    private String orderId;
    private String customerId;
    private Double totalAmount;
    private Double taxAmount;
    private Double grandTotal;
    private Address billingAddress;
    private Instant invoiceDate;
    private InvoiceType type;
    private InvoiceStatus status;
    private Instant createdAt;
    private Instant updatedAt;
}